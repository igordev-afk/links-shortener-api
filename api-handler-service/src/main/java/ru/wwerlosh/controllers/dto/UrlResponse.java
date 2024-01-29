package ru.wwerlosh.controllers.dto;

import ru.wwerlosh.entities.Url;

public class UrlResponse implements Response {

    private String longUrl;
    private String shortUrl;

    public UrlResponse() {
    }

    public UrlResponse(String longUrl, String shortUrl) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
    }

    public UrlResponse(Url url) {
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
