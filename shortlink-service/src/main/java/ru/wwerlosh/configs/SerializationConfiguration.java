package ru.wwerlosh.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.BitSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializationConfiguration {

    @Bean("bitSetObjectMapper")
    public ObjectMapper bitSetObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(new BitSetSerializer());
        module.addDeserializer(BitSet.class, new BitSetDeserializer());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        return mapper;
    }
}
