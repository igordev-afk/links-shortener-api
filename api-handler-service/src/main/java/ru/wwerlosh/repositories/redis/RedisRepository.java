package ru.wwerlosh.repositories.redis;

import ru.wwerlosh.entities.Url;

public interface RedisRepository {
    Url findByLongUrl(String longUrl);
    void save(Url url);
}
