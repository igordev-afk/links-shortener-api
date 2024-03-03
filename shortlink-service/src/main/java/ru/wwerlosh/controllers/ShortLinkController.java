package ru.wwerlosh.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.services.ShortLinkService;

@RestController
public class ShortLinkController {

    private final ShortLinkService service;

    public ShortLinkController(ShortLinkService service) {
        this.service = service;
    }

    @PostMapping("api/shorten")
    public String shortenLink(@RequestBody UrlRequest longUrl) {
        return service.shortenLink(longUrl);
    }
}
