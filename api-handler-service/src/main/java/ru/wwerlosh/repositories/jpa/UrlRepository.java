package ru.wwerlosh.repositories.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import ru.wwerlosh.entities.Url;

import java.util.Optional;

@Component
public interface UrlRepository extends CrudRepository<Url, Long> {
    Optional<Url> findUrlByLongUrl(String longUrl);
    Optional<Url> findUrlByShortUrl(String shortUrl);
}
