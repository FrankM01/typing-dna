package com.typingdna.biometric;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typingdna.dto.request.BiometricRequest;
import com.typingdna.dto.response.EnrollResponse;
import com.typingdna.dto.response.VerifyResponse;
import com.typingdna.exception.BiometricException;
import com.typingdna.exception.BiometricProfileNotFoundException;
import com.typingdna.exception.InternalServerException;
import com.typingdna.history.AuthAttempt;
import com.typingdna.history.AuthAttemptRepository;
import com.typingdna.usuario.Usuario;
import com.typingdna.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BiometricService {
    private final BiometricProfileRepository biometricProfileRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuthAttemptRepository authAttemptRepository;
    private final ObjectMapper objectMapper;

    private static final int REQUIRED_SAMPLES = 5;

    // ── ENROLAMIENTO ──────────────────────────────────────────────

    public EnrollResponse enroll(String username, BiometricRequest request) {
        Usuario usuario = getUsuario(username);
        BiometricProfile profile = biometricProfileRepository
                .findByUsuarioId(usuario.getId())
                .orElse(BiometricProfile.builder().usuario(usuario).samplesCount(0).build());

        List<Double> newDwell = request.dwellVector();
        List<Double> newFlight = request.flightVector();

        if (profile.getSamplesCount() == 0) {
            profile.setDwellVector(toJson(newDwell));
            profile.setFlightVector(toJson(newFlight));
        } else {
            List<Double> avgDwell = average(fromJson(profile.getDwellVector()), newDwell);
            List<Double> avgFlight = average(fromJson(profile.getFlightVector()), newFlight);
            profile.setDwellVector(toJson(avgDwell));
            profile.setFlightVector(toJson(avgFlight));
        }

        profile.setSamplesCount(profile.getSamplesCount() + 1);
        biometricProfileRepository.save(profile);

        boolean complete = profile.getSamplesCount() >= REQUIRED_SAMPLES;
        return EnrollResponse.builder()
                .samplesCount(profile.getSamplesCount())
                .requiredSamples(REQUIRED_SAMPLES)
                .complete(complete)
                .message(complete ? "Perfil biométrico completo" : "Muestra registrada, sigue entrenando")
                .build();
    }

    // ── VERIFICACIÓN ──────────────────────────────────────────────

    public VerifyResponse verify(String username, BiometricRequest request, String ipAddress) {
        Usuario usuario = getUsuario(username);
        BiometricProfile profile = biometricProfileRepository
                .findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new BiometricProfileNotFoundException(usuario.getId()));

        if (profile.getSamplesCount() < REQUIRED_SAMPLES) {
            throw new BiometricException(profile.getSamplesCount());
        }

        double dwellScore = cosineSimilarity(fromJson(profile.getDwellVector()), request.dwellVector());
        double flightScore = cosineSimilarity(fromJson(profile.getFlightVector()), request.flightVector());
        double finalScore = (dwellScore + flightScore) / 2.0;

        double threshold = profile.getThreshold() != null ? profile.getThreshold() : 0.75;
        boolean success = finalScore >= threshold;

        AuthAttempt attempt = AuthAttempt.builder()
                .usuario(usuario)
                .similarityScore(finalScore)
                .success(success)
                .ipAddress(ipAddress) // el controller puede pasarlo si quieres
                .build();
        authAttemptRepository.save(attempt);

        return VerifyResponse.builder()
                .score(Math.round(finalScore * 100.0) / 100.0)
                .success(success)
                .message(success ? "Identidad verificada" : "No coincide el patrón biométrico")
                .build();
    }

    public EnrollResponse getStatus(String username) {
        Usuario usuario = getUsuario(username);
        BiometricProfile profile = biometricProfileRepository
                .findByUsuarioId(usuario.getId())
                .orElse(BiometricProfile.builder().samplesCount(0).build());

        return EnrollResponse.builder()
                .samplesCount(profile.getSamplesCount())
                .requiredSamples(REQUIRED_SAMPLES)
                .complete(profile.getSamplesCount() >= REQUIRED_SAMPLES)
                .message("Estado del perfil biométrico")
                .build();
    }

    // ── ALGORITMO: COSINE SIMILARITY ──────────────────────────────

    private double cosineSimilarity(List<Double> a, List<Double> b) {
        int size = Math.min(a.size(), b.size());
        double dot = 0;
        double normA = 0;
        double normB = 0;

        for (int i = 0; i < size; i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }

        if (normA == 0 || normB == 0) return 0.0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    // ── HELPERS ───────────────────────────────────────────────────

    private List<Double> average(List<Double> existing, List<Double> incoming) {
        int size = Math.min(existing.size(), incoming.size());
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            result.add((existing.get(i) + incoming.get(i)) / 2.0);
        }
        return result;
    }

    private Usuario getUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private String toJson(List<Double> list) {
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    private List<Double> fromJson(String json) {
        try {
            return objectMapper.readValue(json, List.class);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
