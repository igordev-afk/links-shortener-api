package ru.wwerlosh.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;
import ru.wwerlosh.configs.HttpClient;
import ru.wwerlosh.controllers.dto.ErrorResponse;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.controllers.dto.UrlResponse;
import ru.wwerlosh.entities.Url;
import ru.wwerlosh.repositories.redis.RedisRepository;
import ru.wwerlosh.repositories.jpa.UrlRepository;

import java.io.IOException;
import java.util.Optional;

@Service
public class ApiService {

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

        Url cache = redisRepository.findByLongUrl(longUrl);
        if (cache != null) {
            return new UrlResponse(cache);
        }

        try {
            if (!httpClient.tryConnect(urlRequest.getLongUrl())) {
                return new ErrorResponse("Failed to establish connection to the URL");
            }
        } catch (IOException e) {
            return new ErrorResponse("IOException occurred while trying to connect to the URL");
        }

        Optional<Url> urlOptional = urlRepository.findUrlByLongUrl(longUrl);

        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            redisRepository.save(url);
            return new UrlResponse(url);
        }

        String shortUrl = null;
        try {
            shortUrl = httpClient.generateShortLink(urlRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        redisRepository.save(new Url(longUrl, shortUrl));
        return new UrlResponse(longUrl, shortUrl);
    }

    public String redirect(String token) {
        Optional<Url> urlOptional = urlRepository.findUrlByShortUrl(token);

        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            return url.getLongUrl();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Page not found");
        }
    }

}
