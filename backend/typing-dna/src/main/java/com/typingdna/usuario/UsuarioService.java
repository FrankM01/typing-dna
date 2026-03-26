package com.typingdna.usuario;

import com.typingdna.biometric.BiometricProfile;
import com.typingdna.biometric.BiometricProfileRepository;
import com.typingdna.dto.request.ThresholdRequest;
import com.typingdna.dto.response.UpdateUsuarioRequest;
import com.typingdna.dto.response.UsuarioResponse;
import com.typingdna.exception.BiometricProfileNotFoundException;
import com.typingdna.exception.EmailAlreadyExistException;
import com.typingdna.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BiometricProfileRepository biometricProfileRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse getMe(String username) {
        Usuario usuario = getUsuario(username);
        return toResponse(usuario);
    }

    public UsuarioResponse updateMe(String username, UpdateUsuarioRequest request) {
        Usuario usuario = getUsuario(username);

        if (request.email() != null && !request.email().isBlank()) {
            if (usuarioRepository.existsByEmail(request.email())
                    && !usuario.getEmail().equals(request.email())) {
                throw new EmailAlreadyExistException(request.email());
            }
            usuario.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.password()));
        }

        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public void updateThreshold(String username, ThresholdRequest request) {
        Usuario usuario = getUsuario(username);
        BiometricProfile profile = biometricProfileRepository
                .findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new BiometricProfileNotFoundException(usuario.getId()));

        profile.setThreshold(request.threshold());
        biometricProfileRepository.save(profile);
    }

    private Usuario getUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .isActive(usuario.getIsActive())
                .createdAt(usuario.getCreatedAt())
                .build();
    }
}
