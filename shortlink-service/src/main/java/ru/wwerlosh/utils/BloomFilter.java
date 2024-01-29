package ru.wwerlosh.utils;

import com.google.common.hash.Hashing;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

//TODO: redo methods of data serialization/uploading -> downloading
@Component
public class BloomFilter {

    private final int size = 100000;
    private final int numHashFunctions = 4;
    private final int[] hashFunctions;
    private final BitSet bitSet;
    private final ResourceLoader resourceLoader;

    public BloomFilter(ResourceLoader resourceLoader) {
        this.hashFunctions = generateHashFunctions(numHashFunctions);
        this.bitSet = new BitSet(size);
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        loadFromFile();
    }

    @PreDestroy
    public void destroy() {
        saveToFile();
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
            hashFunctions[i] = i + 1; // You can use a more sophisticated hash function here
        }
        return hashFunctions;
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getFileFromResources("bloomFilter.dat")))) {
            byte[] byteArray = serializeBitSet(bitSet);
            oos.writeObject(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getFileFromResources("bloomFilter.dat")))) {
            byte[] byteArray = (byte[]) ois.readObject();
            if (byteArray != null) {
                BitSet loadedBitSet = deserializeBitSet(byteArray);
                bitSet.or(loadedBitSet);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private byte[] serializeBitSet(BitSet bitSet) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(bitSet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();
    }

    private BitSet deserializeBitSet(byte[] byteArray) {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (BitSet) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new BitSet();
    }

    private InputStream getResourceAsStream(String fileName) {
        Resource resource = resourceLoader.getResource("classpath:" + fileName);
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Could not get InputStream from resources: " + fileName, e);
        }
    }

    private File getFileFromResources(String fileName) {
        try {
            InputStream inputStream = getResourceAsStream(fileName);
            File file = File.createTempFile("tempFile", ".dat");
            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            return file;
        } catch (IOException e) {
            throw new RuntimeException("Could not get file from resources: " + fileName, e);
        }
    }
}
