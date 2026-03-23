package com.typingdna.biometric;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BiometricProfileRepository extends JpaRepository<BiometricProfile, Long> {
    Optional<BiometricProfile> findByUsuarioId(Long userId);
}
