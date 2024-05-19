package com.example.demo.services;

import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;

    public ShortenedUrl findByUuid(String uuid) {
        return linkRepository.findByUuid(uuid);
    }

    public void addLink(ShortenedUrl shortenedUrl) {
        linkRepository.save(shortenedUrl);
    }

    public List<ShortenedUrl> listAllUrlsForUser(Long userId) {
        return linkRepository.findAllByUserIdOrderByCreationDateDesc(userId);
    }

}
