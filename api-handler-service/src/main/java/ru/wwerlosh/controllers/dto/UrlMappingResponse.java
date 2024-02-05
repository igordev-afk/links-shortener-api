package ru.wwerlosh.controllers.dto;

import ru.wwerlosh.entities.UrlMapping;

public class UrlMappingResponse implements Response {

    private String longUrl;
    private String shortUrl;

    public UrlMappingResponse() {
    }

    public UrlMappingResponse(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public UrlMappingResponse(UrlMapping url) {
        this.longUrl = url.getLongUrl();
        this.shortUrl = url.getShortUrl();
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }
}
