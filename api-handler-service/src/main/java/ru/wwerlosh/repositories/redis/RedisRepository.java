package ru.wwerlosh.repositories.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.wwerlosh.entities.UrlMapping;
import ru.wwerlosh.utils.UrlMappingSerializer;

@Component
public class RedisRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisRepository.class);

    private static final String HASH_KEY = "URL";
    private final HashOperations<String, String, String> hashOperations;
    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    public String findLongUrlByToken(String token) {
        String longUrl = hashOperations.get(HASH_KEY, token);
        if (longUrl == null) {
            logger.debug("No entry found");
            return null;
        }
        return longUrl;
    }

    public void save(String token, String longUrl) {
        hashOperations.put(HASH_KEY, token, longUrl);
        logger.debug("Saved entry");
    }
}
