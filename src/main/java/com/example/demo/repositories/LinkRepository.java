package com.example.demo.repositories;

import com.example.demo.models.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LinkRepository extends JpaRepository<ShortenedUrl, Long> {

    ShortenedUrl findByUuid(String uuid);

}
