package com.typingdna.auth;

import com.typingdna.dto.request.LoginRequest;
import com.typingdna.dto.request.RegisterRequest;
import com.typingdna.dto.response.AuthResponse;
import com.typingdna.exception.EmailAlreadyExistException;
import com.typingdna.exception.InvalidCredentialsException;
import com.typingdna.exception.UserNotFoundException;
import com.typingdna.exception.UsernameAlreadyExistException;
import com.typingdna.usuario.Usuario;
import com.typingdna.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistException(request.username());
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistException(request.email());
        }

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isActive(true)
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario.getUsername());
        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }

        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException(request.username()));

        String token = jwtService.generateToken(usuario.getUsername());
        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .build();
    }
}
