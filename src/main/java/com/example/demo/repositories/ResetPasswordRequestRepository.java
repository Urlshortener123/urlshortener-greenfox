package com.example.demo.repositories;

import com.example.demo.models.ResetPasswordRequest;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, Long> {
    Optional<ResetPasswordRequest> findByHash(String hash);
    Optional<ResetPasswordRequest> findByUserAndHash(User user, String hash);
    void deleteByUser(User user);
}