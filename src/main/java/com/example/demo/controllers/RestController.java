package com.example.demo.controllers;

import com.example.demo.DTO.MessageDTO;
import com.example.demo.models.ShortenedUrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@Controller
public class RestController {
    @PostMapping("/shortUrl")
    public String shorteningUrl(@PathVariable String Url) {
        if (Url == null) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("No URL provided");
            return "No url provided";
            //to be done w flash attributes?
        }
        if (Url.isEmpty()) {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessage("No URL provided");
            //dto is also created in case it is needed in the  future
            return "No url provided";
            //to be done w flash attributes?
        }
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setUrl(Url);
        UUID uuid = UUID.randomUUID();
        String textUuid = uuid.toString();
        shortenedUrl.setShortenedUrl(textUuid);
        // return flash attr? with links?
        // once the Docker database is implemented object is to be  saved

        return "redirect/index";
    }

}
