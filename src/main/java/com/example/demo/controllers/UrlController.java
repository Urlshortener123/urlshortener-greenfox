package com.example.demo.controllers;

import com.example.demo.models.ShortenedUrl;
import com.example.demo.models.User;
import com.example.demo.services.BlockerService;
import com.example.demo.services.LinkService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UrlController {

    private final LinkService linkService;
    private final UserService userService;
    private final BlockerService blockerService;

    @Value("${domain.name}")
    private String domainName;

    @PostMapping("/shortUrl")
    public String shorteningUrl(@RequestParam String url,
                                @RequestParam String username,
                                RedirectAttributes redirectAttributes) {
        // Check if url is given as parameter
        if (url == null || url.isEmpty()) {
            redirectAttributes.addFlashAttribute("missingUrlError", "No URL is provided!");
            return "redirect:/index";
        }

        // Check whether the url is malicious
        if (blockerService.IsMalicious(url)) {
            redirectAttributes.addFlashAttribute("maliciousError","The given URL is considered malicious, shortening is not possible");
            return "redirect:/index";
        }

        // Perform the url shortening
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
        linkService.addLink(shortenedUrl);
        redirectAttributes.addFlashAttribute("longUrl", (domainName) + "/r/" + textUuid);
        return "redirect:/index";
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/r/{uuid}")
    public void redirectToUrl(@PathVariable(name = "uuid") String hash,
                              HttpServletResponse response) {
        ShortenedUrl shortenedUrl = linkService.findByUuid(hash);
        response.addHeader("location", shortenedUrl.getUrl());
    }

    @GetMapping("/history")
    public String historyPage(Model model, Principal principal) {
        User actUser = userService.selectUser(principal.getName());
        model.addAttribute("urls", linkService.listAllUrlsForUser(actUser.getId()));
        model.addAttribute("domain", domainName);
        return "history";
    }

}
