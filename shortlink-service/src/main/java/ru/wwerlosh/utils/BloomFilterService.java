package ru.wwerlosh.utils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.BitSet;
import org.springframework.stereotype.Service;

@Service
public class BloomFilterService {

    private final BloomFilter bloomFilter;
    private final BloomFilterFileHandler fileHandler;

    public BloomFilterService(BloomFilter bloomFilter, BloomFilterFileHandler fileHandler) {
        this.bloomFilter = bloomFilter;
        this.fileHandler = fileHandler;
    }

    @PostConstruct
    private void init() {
        BitSet downloaded = fileHandler.load();
        bloomFilter.setBitSet(downloaded);
    }

    @PreDestroy
    private void destroy() {
        BitSet uploaded = bloomFilter.getBitSet();
        fileHandler.save(uploaded);
    }
}
