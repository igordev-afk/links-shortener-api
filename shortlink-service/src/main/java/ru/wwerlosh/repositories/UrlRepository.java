package ru.wwerlosh.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.wwerlosh.entities.Url;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Url findUrlByShortUrl(String shortUrl);
}
