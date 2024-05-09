package com.example.demo.controllers;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.models.ShortenedUrl;
import com.example.demo.repositories.LinkRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UrlController {

    private final LinkRepository linkRepository;

    @Value("${domain.name}")
    private String name = "placeholder name";

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
        shortenedUrl.setUuid(textUuid);
        linkRepository.save(shortenedUrl);
        redirectAttributes.addFlashAttribute("longUrl", (name) + "/r/" + textUuid);
        return "redirect:/index";
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/r/{uuid}")
    public void redirectToUrl(@PathVariable(name = "uuid") String hash,
                              HttpServletResponse response) {
        ShortenedUrl shortenedUrl = linkRepository.findByUuid(hash);
        response.addHeader("location", shortenedUrl.getUrl());
    }

}
