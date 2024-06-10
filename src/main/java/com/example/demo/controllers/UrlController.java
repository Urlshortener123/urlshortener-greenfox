package com.example.demo.controllers;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.models.ShortenedUrl;
import com.example.demo.models.User;
import com.example.demo.repositories.LinkRepository;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class UrlController {

    private final LinkRepository linkRepository;
    private final UserService userService;

    @Value("${base.url}")
    private String baseUrl;

    @PostMapping("/shortUrl")
    public String shorteningUrl(@RequestParam String url,
                                @RequestParam String username,
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
        shortenedUrl.setCreationDate(LocalDate.now());
        if (!username.isEmpty()) {
            User actUser = userService.selectUser(username);
            shortenedUrl.setUser(actUser);
        }
        linkRepository.save(shortenedUrl);
        redirectAttributes.addFlashAttribute("longUrl", String.format("%s/r/%s", baseUrl, textUuid));
        return "redirect:/index";
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/r/{uuid}")
    public void redirectToUrl(@PathVariable(name = "uuid") String hash,
                              HttpServletResponse response) throws IOException {
        ShortenedUrl shortenedUrl = linkRepository.findByUuid(hash);

        if (shortenedUrl != null) {
            shortenedUrl.setClickCount(shortenedUrl.getClickCount() + 1);
            linkRepository.save(shortenedUrl);
            response.addHeader("location", shortenedUrl.getUrl());
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "URL not found");
        }
    }

    @GetMapping("/history")
    public String historyPage(Model model, Principal principal) {
        User actUser = userService.selectUser(principal.getName());
        model.addAttribute("urls", linkRepository.findAllByUserIdOrderByCreationDateDesc(actUser.getId()));
        model.addAttribute("domain", baseUrl);
        return "history";
    }

}
