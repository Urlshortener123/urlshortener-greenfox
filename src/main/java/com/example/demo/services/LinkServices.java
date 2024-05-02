package com.example.demo.services;

import com.example.demo.repositories.LinkRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkServices {

    private final LinkRepository linkRepository;
//find by UUID to be created
}
