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
import ru.wwerlosh.repositories.redis.RedisRepository;
import ru.wwerlosh.repositories.jpa.UrlRepository;

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

        UrlMapping cache = getFromCache(longUrl);
        if (cache != null) {
            return new UrlMappingResponse(cache);
        }

        if (!tryConnectToUrl(longUrl)) {
            return new ErrorResponse("Failed to establish connection to the URL");
        }

        UrlMapping databaseUrl = getFromDatabase(longUrl);
        if (databaseUrl != null) {
            return new UrlMappingResponse(databaseUrl);
        }

        String shortUrl = generateAndSaveShortLink(urlRequest);
        return new UrlMappingResponse(longUrl, shortUrl);
    }

    public String redirect(String token) {
        Optional<UrlMapping> urlOptional = urlRepository.findUrlMappingByShortUrl(token);

        if (urlOptional.isPresent()) {
            UrlMapping url = urlOptional.get();
            logger.info("Redirecting to long URL: '{}'", url.getLongUrl());
            return url.getLongUrl();
        } else {
            logger.warn("Failed to redirect. Invalid or expired token: '{}'", token);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found");
        }
    }

    private UrlMapping getFromCache(String longUrl) {
        UrlMapping cache = redisRepository.findByLongUrl(longUrl);
        if (cache != null) {
            logger.info("Shortened link found in cache for '{}'", longUrl);
        }
        return cache;
    }

    private boolean tryConnectToUrl(String longUrl) {
        try {
            if (!httpClient.tryConnect(longUrl)) {
                logger.warn("Failed to establish connection to the URL: '{}'", longUrl);
                return false;
            }
        } catch (IOException e) {
            logger.error("IOException occurred while trying to connect to the URL: '{}'", longUrl, e);
            return false;
        }
        return true;
    }

    private UrlMapping getFromDatabase(String longUrl) {
        Optional<UrlMapping> urlOptional = urlRepository.findUrlMappingByLongUrl(longUrl);
        if (urlOptional.isPresent()) {
            UrlMapping url = urlOptional.get();
            redisRepository.save(url);
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
        redisRepository.save(new UrlMapping(longUrl, shortUrl));
        logger.info("Generated and saved short link for '{}'", longUrl);
        return shortUrl;
    }

}
