package com.typingdna.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthAttemptRepository extends JpaRepository<AuthAttempt, Long> {
    List<AuthAttempt> findByUsuarioIdOrderByAttemptedAtDesc(Long userId);
    List<AuthAttempt> findTop10ByUsuarioIdOrderByAttemptedAtDesc(Long userId);
}
