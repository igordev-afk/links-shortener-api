package ru.wwerlosh.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BloomFilterFileHandler {

    private final static Logger logger = LoggerFactory.getLogger(BloomFilterFileHandler.class);

    private final ObjectMapper mapper;
    private final String JSON_PATH;

    public BloomFilterFileHandler(@Qualifier("bitSetObjectMapper") ObjectMapper mapper,
                                  @Value("${spring.data.bloom-filter.bitset-json-path}") String JSON_PATH) {
        this.mapper = mapper;
        this.JSON_PATH = JSON_PATH;
    }

    protected BitSet load() {
        File file = new File(JSON_PATH);
        if (!file.exists() || file.length() == 0) {
            logger.debug("File doesn't exists or his length is 0, return new BitSet()");
            return new BitSet();
        }

        try {
            BitSet bitSet = mapper.readValue(file, new TypeReference<BitSet>() {});
            logger.debug("File is successfully read");
            return bitSet;
        } catch (IOException e) {
            logger.warn(e.getMessage());
            return new BitSet();
        }
    }

    protected void save(BitSet bitSet) {
        try {
            mapper.writeValue(new File(JSON_PATH), bitSet);
            logger.debug("File is successfully written");
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }
}
