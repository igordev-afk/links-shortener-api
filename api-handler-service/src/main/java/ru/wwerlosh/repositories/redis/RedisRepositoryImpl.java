package ru.wwerlosh.repositories.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.wwerlosh.entities.Url;
import ru.wwerlosh.utils.UrlObjectMapper;

@Component
public class RedisRepositoryImpl implements RedisRepository {

    private static final String HASH_KEY = "URL";

    private final HashOperations<String, String, String> hashOperations;

    private final UrlObjectMapper objectMapper;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate,
                               UrlObjectMapper objectMapper) {
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    @Override
    public Url findByLongUrl(String longUrl) {
        String hash = hashOperations.get(HASH_KEY, longUrl);
        if (hash == null) return null;
        return objectMapper.convertToUrl(hash);
    }

    @Override
    public void save(Url url) {
        String hash = objectMapper.convertToJson(url);
        hashOperations.put(HASH_KEY, url.getLongUrl(), hash);
    }
}
