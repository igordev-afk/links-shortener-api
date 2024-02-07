package ru.wwerlosh.utils;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BloomFilter {

    private final int size;
    private final int[] hashFunctions;

    private BitSet bitSet;

    public BloomFilter(@Value("${spring.data.bloom-filter.size}") int size,
                       @Value("${spring.data.bloom-filter.num-hash-functions}") int numHashFunctions) {
        this.size = size;
        this.hashFunctions = generateHashFunctions(numHashFunctions);
    }

    protected void setBitSet(BitSet bitSet) {
        this.bitSet = bitSet;
    }

    protected BitSet getBitSet() {
        return bitSet;
    }

    public void add(String value) {
        for (int hashFunction : hashFunctions) {
            int index = hash(value, hashFunction);
            bitSet.set(index);
        }
    }

    public boolean contains(String value) {
        for (int hashFunction : hashFunctions) {
            int index = hash(value, hashFunction);
            if (!bitSet.get(index)) {
                return false;
            }
        }
        return true;
    }

    private int hash(String value, int hashFunction) {
        return Math.abs(Hashing.murmur3_128(hashFunction).hashString(value, StandardCharsets.UTF_8).asInt() % size);
    }

    private int[] generateHashFunctions(int numHashFunctions) {
        int[] hashFunctions = new int[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            hashFunctions[i] = i + 1;
        }
        return hashFunctions;
    }

}
