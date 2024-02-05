package ru.wwerlosh.repositories.redis;

import ru.wwerlosh.entities.UrlMapping;

public interface RedisRepository {
    UrlMapping findByLongUrl(String longUrl);
    void save(UrlMapping url);
}
