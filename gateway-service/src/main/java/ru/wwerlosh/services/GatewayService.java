package ru.wwerlosh.services;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import ru.wwerlosh.configs.HttpClient;
import ru.wwerlosh.configs.ResponseDeserializer;
import ru.wwerlosh.controllers.dto.AuthorizationErrorResponse;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.controllers.dto.UrlResponse;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationDTO;
import ru.wwerlosh.exceptions.AuthorizationException;
import ru.wwerlosh.exceptions.InvalidTokenException;
import ru.wwerlosh.utils.JwtUtils;

@Service
public class GatewayService {

    private final HttpClient httpClient;
    private final JwtUtils jwtUtils;
    private final String HOST;
    public GatewayService(HttpClient httpClient, JwtUtils jwtUtils,
                          @Value("${server.uri}") String HOST) {
        this.httpClient = httpClient;
        this.jwtUtils = jwtUtils;
        this.HOST = HOST;
    }


    public JwtAuthenticationDTO signUp(SignUpRequest request) {
        return httpClient.signUpClient(request);
    }

    public JwtAuthenticationDTO signIn(SignInRequest request) {
        return httpClient.signInClient(request);
    }

    public Response shortenUrl(String jwt, UrlRequest request) {

        if (!httpClient.validateTokenClient(jwt)) {
            throw new InvalidTokenException("Token is invalid or has been expired", jwt);
        }

        Response response = httpClient.shortenUrlClient(request);
        if (response.getClass() == UrlResponse.class) {
            String shortUrl = HOST + ((UrlResponse) response).getShortUrl();
            ((UrlResponse) response).setShortUrl(shortUrl);
        }

        return response;
    }

    public RedirectView redirect(String token) {
        String url = httpClient.redirectClient(token);
        return new RedirectView(url);
    }
}
