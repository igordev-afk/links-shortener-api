package ru.wwerlosh.services;

import org.springframework.stereotype.Service;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.entities.UrlMapping;
import ru.wwerlosh.repositories.UrlMappingRepository;
import ru.wwerlosh.utils.BloomFilter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class ShortLinkServiceImpl implements ShortLinkService {

    private final UrlMappingRepository repository;
    private final BloomFilter bloomFilter;

    public ShortLinkServiceImpl(UrlMappingRepository repository, BloomFilter bloomFilter) {
        this.repository = repository;
        this.bloomFilter = bloomFilter;
    }

    @Override
    public String shortenLink(UrlRequest longUrl) {

        String encodedHash = generateShortUrl(longUrl.getLongUrl());

        while (bloomFilter.contains(encodedHash)) {
            encodedHash = generateShortUrl(encodedHash + longUrl.getLongUrl());
        }

        bloomFilter.add(encodedHash);
        repository.save(new UrlMapping(longUrl.getLongUrl(), encodedHash));
        return encodedHash;
    }

    public static String generateShortUrl(String longUrl) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(longUrl.getBytes());

            String base64 = Base64.getEncoder().encodeToString(digest);

            base64 = base64.replaceAll("[^a-zA-Z0-9]", "");

            return base64.substring(0, 7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
