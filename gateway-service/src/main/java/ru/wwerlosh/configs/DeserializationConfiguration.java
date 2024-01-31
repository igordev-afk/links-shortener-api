package ru.wwerlosh.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.wwerlosh.controllers.dto.Response;

@Configuration
public class DeserializationConfiguration {

    @Bean("urlResponseObjectMapper")
    public ObjectMapper urlResponseObjectMapper() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Response.class, new ResponseDeserializer());
        return new ObjectMapper().registerModules(module);
    }

}
