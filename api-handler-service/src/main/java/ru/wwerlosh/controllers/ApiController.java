package ru.wwerlosh.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.controllers.dto.UrlResponse;
import ru.wwerlosh.services.ApiService;

@RestController
public class ApiController {

    public static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @PostMapping("api/v1/data/shorten")
    public Response shortenLink(@RequestBody UrlRequest longUrl) {
        logger.info("New request");
        return apiService.shortenLink(longUrl);
    }

    @GetMapping("{token}")
    public RedirectView redirect(@PathVariable("token") String token) {
        return apiService.redirect(token);
    }
}
