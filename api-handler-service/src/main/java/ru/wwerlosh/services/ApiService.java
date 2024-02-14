package ru.wwerlosh.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.wwerlosh.configs.HttpClient;
import ru.wwerlosh.controllers.dto.ErrorResponse;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlMappingResponse;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.entities.UrlMapping;
import ru.wwerlosh.repositories.jpa.UrlRepository;
import ru.wwerlosh.repositories.redis.RedisRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private final UrlRepository urlRepository;
    private final RedisRepository redisRepository;
    private final HttpClient httpClient;

    public ApiService(UrlRepository urlRepository,
                      RedisRepository redisRepository,
                      HttpClient httpClient) {
        this.urlRepository = urlRepository;
        this.httpClient = httpClient;
        this.redisRepository = redisRepository;
    }

    public Response shortenLink(UrlRequest urlRequest) {
        String longUrl = urlRequest.getLongUrl();

        if (!checkConnection(longUrl)) {
            return new ErrorResponse("Failed to establish connection to the URL");
        }

        UrlMapping databaseUrl = getUrlMappingFromDatabase(longUrl);
        if (databaseUrl != null) {
            return new UrlMappingResponse(databaseUrl);
        }

        String shortUrl = generateAndSaveShortLink(urlRequest);
        return new UrlMappingResponse(longUrl, shortUrl);
    }

    public String redirect(String token) {
        String longUrl = redisRepository.findLongUrlByToken(token);
        if (longUrl != null) {
            return longUrl;
        }

        Optional<UrlMapping> urlOptional = urlRepository.findUrlMappingByShortUrl(token);
        if (urlOptional.isPresent()) {
            UrlMapping url = urlOptional.get();
            redisRepository.save(url.getShortUrl(), url.getLongUrl());
            logger.info("Redirecting to long URL: '{}'", url.getLongUrl());
            return url.getLongUrl();
        } else {
            logger.warn("Failed to redirect. Invalid or expired token: '{}'", token);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found");
        }
    }

    private boolean checkConnection(String longUrl) {
        try {
            if (!httpClient.checkHttpOk(longUrl)) {
                logger.warn("Failed to establish connection to the URL: '{}'", longUrl);
                return false;
            }
        } catch (IOException e) {
            logger.error("IOException occurred while trying to connect to the URL: '{}'", longUrl, e);
            return false;
        }
        return true;
    }

    private UrlMapping getUrlMappingFromDatabase(String longUrl) {
        Optional<UrlMapping> urlOptional = urlRepository.findUrlMappingByLongUrl(longUrl);
        if (urlOptional.isPresent()) {
            UrlMapping url = urlOptional.get();
            logger.info("Shortened link retrieved from database for '{}'", longUrl);
            return url;
        }
        return null;
    }

    private String generateAndSaveShortLink(UrlRequest urlRequest) {
        String longUrl = urlRequest.getLongUrl();
        String shortUrl;
        try {
            shortUrl = httpClient.generateShortLink(urlRequest);
        } catch (IOException e) {
            logger.error("IOException occurred while trying to generate short link for '{}'", longUrl, e);
            throw new RuntimeException(e);
        }
        logger.info("Generated and saved short link for '{}'", longUrl);
        return shortUrl;
    }

}
