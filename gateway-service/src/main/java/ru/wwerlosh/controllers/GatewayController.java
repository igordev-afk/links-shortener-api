package ru.wwerlosh.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
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
    public ResponseEntity<?> signUp(@RequestBody SignUpRequest request, HttpServletResponse response) {
        JwtAuthenticationDTO authDto = gatewayService.signUp(request);
        Cookie cookie = new Cookie("token", authDto.getToken());//создаем объект Cookie,
        //в конструкторе указываем значения для name и value
        cookie.setPath("/");//устанавливаем путь
        cookie.setMaxAge(86400);//здесь устанавливается время жизни куки
        response.addCookie(cookie);//добавляем Cookie в запрос
        response.setContentType("text/plain");//устанавливаем контекст
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("/signin")
    public JwtAuthenticationDTO signIn(@RequestBody SignInRequest request) {
        return gatewayService.signIn(request);
    }

    @PostMapping("/shorten_url")
    public Response shortenUrl(@CookieValue(value = "token") String token,
                                               @RequestBody UrlRequest request) {
        return gatewayService.shortenUrl(token, request);
    }

    @GetMapping("/{token}")
    public RedirectView redirect(@PathVariable("token") String token) {
        return gatewayService.redirect(token);
    }
}
