package ru.wwerlosh.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
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
        logger.info("Received request to shorten link: '{}'", longUrl.getLongUrl());
        Response response = apiService.shortenLink(longUrl);
        logger.info("Shortened link response: {}", response);
        return response;
    }

    @GetMapping("api/v1/data/{token}")
    public String redirect(@PathVariable("token") String token) {
        logger.info("Received request to redirect with token: '{}'", token);
        String destinationUrl = apiService.redirect(token);

        if (destinationUrl != null) {
            logger.info("Redirecting to: '{}'", destinationUrl);
            return destinationUrl;
        } else {
            logger.warn("Failed to redirect. Invalid or expired token: '{}'", token);
            return "redirect:/error";
        }
    }
}
