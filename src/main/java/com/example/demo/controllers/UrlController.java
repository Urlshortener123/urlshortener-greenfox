package com.example.demo.controllers;

import com.example.demo.DTO.UrlRequest;
import com.example.demo.models.ShortenedUrl;
import com.example.demo.models.User;
import com.example.demo.services.BlockerService;
import com.example.demo.services.LinkService;
import com.example.demo.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
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
    public String shorteningUrl(@Valid UrlRequest urlRequest,
                                BindingResult bindingResult,
                                Principal principal,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        String url = urlRequest.getUrl();
        // Check whether the url is malicious
        if (blockerService.isMalicious(url)) {
            model.addAttribute("maliciousError", "The given URL is considered malicious, shortening is not possible");
            return "index";
        }

        // Perform the url shortening
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setUrl(url);
        UUID uuid = UUID.randomUUID();
        String textUuid = uuid.toString();
        shortenedUrl.setUuid(textUuid);
        shortenedUrl.setCreationDate(LocalDate.now());
        if (principal != null) {
            User actUser = userService.selectUser(principal.getName());
            shortenedUrl.setUser(actUser);
        }
        linkService.addLink(shortenedUrl);
        redirectAttributes.addFlashAttribute("longUrl", (domainName) + "/r/" + textUuid);
        return "redirect:/index";
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("/r/{uuid}")
    public void redirectToUrl(@PathVariable(name = "uuid") String hash,
                              HttpServletResponse response) throws IOException {
        ShortenedUrl shortenedUrl = linkService.findByUuid(hash);
        String originalUrl = shortenedUrl.getUrl();
        if (shortenedUrl != null) {
            shortenedUrl.setClickCount(shortenedUrl.getClickCount() + 1);
            linkService.addLink(shortenedUrl);
            response.addHeader("location", originalUrl);
        } else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "URL not found");
        }
    }

    @GetMapping("/history")
    public String historyPage(Model model, Principal principal) {
        User actUser = userService.selectUser(principal.getName());
        model.addAttribute("urls", linkService.listAllUrlsForUser(actUser.getId()));
        model.addAttribute("domain", domainName);
        return "history";
    }

}
