package com.typingdna.history;

import com.typingdna.dto.response.AuthAttemptResponse;
import com.typingdna.dto.response.StatsResponse;
import com.typingdna.usuario.Usuario;
import com.typingdna.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final AuthAttemptRepository authAttemptRepository;
    private final UsuarioRepository usuarioRepository;

    public List<AuthAttemptResponse> getAttempts(String username, int limit) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<AuthAttempt> attempts = limit > 0
                ? authAttemptRepository.findTopNByUsuarioIdOrderByCreatedAtDesc(usuario.getId(), PageRequest.of(0, limit))
                : authAttemptRepository.findByUsuarioIdOrderByCreatedAtDesc(usuario.getId());

        return attempts.stream()
                .map(this::toResponse)
                .toList();
    }

    public StatsResponse getStats(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<AuthAttempt> attempts = authAttemptRepository
                .findByUsuarioIdOrderByCreatedAtDesc(usuario.getId());

        if (attempts.isEmpty()) {
            return StatsResponse.builder()
                    .totalAttempts(0)
                    .successRate(0.0)
                    .averageScore(0.0)
                    .build();
        }

        long successes = attempts.stream().filter(AuthAttempt::getSuccess).count();
        double successRate = (double) successes / attempts.size() * 100;
        double avgScore = attempts.stream()
                .mapToDouble(AuthAttempt::getSimilarityScore)
                .average()
                .orElse(0.0);

        return StatsResponse.builder()
                .totalAttempts(attempts.size())
                .successRate(Math.round(successRate * 10.0) / 10.0)
                .averageScore(Math.round(avgScore * 100.0) / 100.0)
                .build();
    }

    private AuthAttemptResponse toResponse(AuthAttempt attempt) {
        return AuthAttemptResponse.builder()
                .id(attempt.getId())
                .similarityScore(attempt.getSimilarityScore())
                .success(attempt.getSuccess())
                .ipAddress(attempt.getIpAddress())
                .createdAt(attempt.getAttemptedAt())
                .build();
    }
}
