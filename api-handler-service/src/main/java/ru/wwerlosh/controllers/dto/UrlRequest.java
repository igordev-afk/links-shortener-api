package ru.wwerlosh.controllers.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.PropertySource;

public class UrlRequest {
    @JsonProperty("long_url")
    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
