package com.typingdna.history;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthAttemptRepository extends JpaRepository<AuthAttempt, Long> {
    List<AuthAttempt> findByUsuarioIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT a FROM AuthAttempt a WHERE a.usuario.id = :userId ORDER BY a.createdAt DESC")
    List<AuthAttempt> findTopNByUsuarioIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
