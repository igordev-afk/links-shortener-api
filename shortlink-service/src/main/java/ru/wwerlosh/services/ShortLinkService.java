package ru.wwerlosh.services;

import ru.wwerlosh.controllers.dto.UrlRequest;

public interface ShortLinkService {

    String shortenLink(UrlRequest longUrl);
}
