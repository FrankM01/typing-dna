package com.typingdna.history;

import com.typingdna.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_attempts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

    @Column(name = "similarity_score", nullable = false)
    private Double similarityScore;

    @Column(nullable = false)
    private Boolean success;

    @Column(name = "ip_address")
    private String ipAddress;

    @CreatedDate
    @Column(name = "attempted_at", nullable = false, updatable = false)
    private LocalDateTime attemptedAt;
}
