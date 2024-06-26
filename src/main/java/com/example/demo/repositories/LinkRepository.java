package com.example.demo.repositories;

import com.example.demo.models.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LinkRepository extends JpaRepository<ShortenedUrl, Long> {

    ShortenedUrl findByUuid(String uuid);

    List<ShortenedUrl> findAllByUserIdOrderByCreationDateDesc(Long userId);

}
