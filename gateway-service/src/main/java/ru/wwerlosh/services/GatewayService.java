package ru.wwerlosh.services;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import ru.wwerlosh.configs.HttpClient;
import ru.wwerlosh.controllers.dto.AuthorizationErrorResponse;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.controllers.dto.UrlResponse;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationDTO;
import ru.wwerlosh.utils.JwtUtils;

@Service
public class GatewayService {

    private final HttpClient httpClient;
    private final JwtUtils jwtUtils;

    public GatewayService(HttpClient httpClient, JwtUtils jwtUtils) {
        this.httpClient = httpClient;
        this.jwtUtils = jwtUtils;
    }


    public JwtAuthenticationDTO signUp(SignUpRequest request) {
        return httpClient.signUpClient(request);
    }

    public JwtAuthenticationDTO signIn(SignInRequest request) {
        return httpClient.signInClient(request);
    }

    public Response shortenUrl(HttpServletRequest servletRequest, UrlRequest request) {

        final String authHeader = servletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new AuthorizationErrorResponse(
                    "You don't have access",
                    HttpStatus.SC_FORBIDDEN
            );
        }

        final String jwtToken = authHeader.substring(7);
        if (jwtUtils.isTokenExpired(jwtToken)) {
            return new AuthorizationErrorResponse(
                    "Token has been expired",
                    HttpStatus.SC_UNAUTHORIZED
            );
        }

        return httpClient.shortenUrlClient(request);
    }

    public RedirectView redirect(String token) {
        String url = httpClient.redirectClient(token);
        return new RedirectView(url);
    }
}
