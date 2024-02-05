package ru.wwerlosh.repositories.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.wwerlosh.entities.UrlMapping;
import ru.wwerlosh.utils.UrlMappingSerializer;

@Component
public class RedisRepositoryImpl implements RedisRepository {

    private static final Logger logger = LoggerFactory.getLogger(RedisRepositoryImpl.class);

    private static final String HASH_KEY = "URL";
    private final HashOperations<String, String, String> hashOperations;
    private final UrlMappingSerializer objectMapper;

    public RedisRepositoryImpl(RedisTemplate<String, String> redisTemplate,
                               UrlMappingSerializer objectMapper) {
        this.hashOperations = redisTemplate.opsForHash();
        this.objectMapper = objectMapper;
    }

    @Override
    public UrlMapping findByLongUrl(String longUrl) {
        String hash = hashOperations.get(HASH_KEY, longUrl);
        if (hash == null) {
            logger.debug("No entry found for long URL: '{}'", longUrl);
            return null;
        }
        UrlMapping url = objectMapper.convertToUrlMapping(hash);
        logger.debug("Found entry for long URL '{}': {}", longUrl, url);
        return url;
    }

    @Override
    public void save(UrlMapping url) {
        String hash = objectMapper.convertToJson(url);
        hashOperations.put(HASH_KEY, url.getLongUrl(), hash);
        logger.debug("Saved entry for long URL '{}'", url.getLongUrl());
    }
}
