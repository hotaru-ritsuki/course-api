package com.example.courseapi.repository;

import com.example.courseapi.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByTokenAndUserEmail(String token, String email);

    int deleteAllByUserId(Long userId);
}
