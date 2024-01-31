package ru.wwerlosh.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationDTO;
import ru.wwerlosh.services.GatewayService;

@RestController
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationDTO> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(gatewayService.signUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationDTO> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(gatewayService.signIn(request));
    }

    @PostMapping("/shorten_url")
    public ResponseEntity<Response> shortenUrl(HttpServletRequest servletRequest,
                                               @RequestBody UrlRequest request) {
        return ResponseEntity.ok(gatewayService.shortenUrl(servletRequest, request));
    }

    @GetMapping("/{token}")
    public RedirectView redirect(@PathVariable("token") String token) {
        return gatewayService.redirect(token);
    }
}
