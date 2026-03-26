package com.typingdna.usuario;

import com.typingdna.dto.request.ThresholdRequest;
import com.typingdna.dto.response.UpdateUsuarioRequest;
import com.typingdna.dto.response.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(usuarioService.getMe(userDetails.getUsername()));
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> updateMe(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.updateMe(userDetails.getUsername(), request));
    }

    @PutMapping("/threshold")
    public ResponseEntity<Void> updateThreshold(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody ThresholdRequest request) {
        usuarioService.updateThreshold(userDetails.getUsername(), request);
        return ResponseEntity.ok().build();
    }
}
