package ru.wwerlosh.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationResponse;
import ru.wwerlosh.services.GatewayService;

@RestController
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("/signup")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody SignUpRequest request) {
        return ResponseEntity.ok(gatewayService.signUp(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest request) {
        return ResponseEntity.ok(gatewayService.signIn(request));
    }
}
