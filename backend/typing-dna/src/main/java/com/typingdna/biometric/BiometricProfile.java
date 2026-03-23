package com.typingdna.biometric;

import com.typingdna.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "biometric_profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BiometricProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "dwell_vector", nullable = false, columnDefinition = "jsonb")
    private String dwellVector;

    @Column(name = "flight_vector", nullable = false, columnDefinition = "jsonb")
    private String flightVector;

    @Builder.Default
    @Column(name = "samples_count")
    private Integer samplesCount = 0;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
