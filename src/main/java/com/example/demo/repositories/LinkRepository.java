package com.example.demo.repositories;

import com.example.demo.models.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LinkRepository extends JpaRepository<ShortenedUrl, Long> {

    ShortenedUrl findByUuid(String uuid);
}
