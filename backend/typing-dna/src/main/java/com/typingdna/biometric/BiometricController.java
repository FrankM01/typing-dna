package com.typingdna.biometric;

import com.typingdna.dto.request.BiometricRequest;
import com.typingdna.dto.response.EnrollResponse;
import com.typingdna.dto.response.VerifyResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/biometric")
@RequiredArgsConstructor
public class BiometricController {
    private final BiometricService biometricService;

    @PostMapping("/enroll")
    public ResponseEntity<EnrollResponse> enroll(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BiometricRequest request,
            HttpServletRequest httpRequest) {
        String ip = getClientIp(httpRequest);
        return ResponseEntity.ok(biometricService.enroll(userDetails.getUsername(), request, ip));
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verify(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BiometricRequest request) {
        return ResponseEntity.ok(biometricService.verify(userDetails.getUsername(), request));
    }

    @GetMapping("/status")
    public ResponseEntity<EnrollResponse> status(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(biometricService.getStatus(userDetails.getUsername()));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
