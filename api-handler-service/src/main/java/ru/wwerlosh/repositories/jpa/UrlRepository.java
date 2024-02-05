package ru.wwerlosh.repositories.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import ru.wwerlosh.entities.UrlMapping;

@Component
public interface UrlRepository extends CrudRepository<UrlMapping, Long> {
    Optional<UrlMapping> findUrlMappingByLongUrl(String longUrl);
    Optional<UrlMapping> findUrlMappingByShortUrl(String shortUrl);
}
