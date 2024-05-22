package com.example.demo.services;

import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public ShortenedUrl findByUuid(String uuid){
        return linkRepository.findByUuid(uuid);
    }
}
