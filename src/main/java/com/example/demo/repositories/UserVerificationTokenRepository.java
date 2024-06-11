package com.example.demo.repositories;

import com.example.demo.models.UserVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationTokenRepository extends JpaRepository<UserVerificationToken, Long> {

    UserVerificationToken findByHash(String hash);

}
