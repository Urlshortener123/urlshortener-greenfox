package com.example.demo.controllers;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UrlController {
    private final LinkRepository linkRepository;

    @PostMapping("/shortUrl")
    public String shorteningUrl(@RequestParam String url,
                                RedirectAttributes redirectAttributes) {
        if (url == null) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("No URL provided");
            return "No url provided";
        }
        if (url.isEmpty()) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("No URL provided");
            return "No url provided";
        }
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setUrl(url);
        UUID uuid = UUID.randomUUID();
        String textUuid = uuid.toString();
        shortenedUrl.setShortenedUrl(textUuid);
        linkRepository.save(shortenedUrl);
        redirectAttributes.addFlashAttribute("longUrl", ("${domain.name}") + "/r/" + textUuid);
        return "redirect/index";
    }

    @GetMapping("/r/{uuid}")
    public RedirectView redirectToUrl(@PathVariable String uuid) {
        ShortenedUrl  shortenedUrl = linkRepository.findByShortenedUrl(uuid);
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(shortenedUrl.getUrl());
        return redirectView;
    }

}
