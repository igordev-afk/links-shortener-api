package ru.wwerlosh.configs;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.wwerlosh.controllers.dto.ErrorResponse;
import ru.wwerlosh.controllers.dto.Response;
import ru.wwerlosh.controllers.dto.UrlRequest;
import ru.wwerlosh.controllers.dto.UrlResponse;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationDTO;
import ru.wwerlosh.exceptions.AuthorizationException;
import ru.wwerlosh.utils.DtoMapper;

@Configuration
public class HttpClient {

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private final DtoMapper mapper;
    private final static CloseableHttpClient httpClient = HttpClients.createDefault();

    @Value("${spring.data.api-handler-service}")
    private String API_HANDLER_SERVICE_URL;

    @Value("${spring.data.auth-service}")
    private String AUTH_SERVICE_URL;

    public HttpClient(DtoMapper mapper) {
        this.mapper = mapper;
    }

    public JwtAuthenticationDTO signUpClient(SignUpRequest signUpRequest) {
        final String serviceEndpoint = AUTH_SERVICE_URL + "/api/v1/auth/signup";

        HttpPost request = new HttpPost(serviceEndpoint);
        request.setHeader("Content-Type", "application/json");

        StringEntity entity = null;
        try {
            entity = new StringEntity(mapper.convertSignupRequestToJson(signUpRequest));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            logger.info("HTTP Status Code: {}", response.getStatusLine().getStatusCode());
            return mapper.convertToJwtObject(
                    EntityUtils.toString(response.getEntity())
            );
        } catch (IOException e) {
            throw new AuthorizationException("Bad request");
        }
    }

    public JwtAuthenticationDTO signInClient(SignInRequest signInRequest) {
        final String serviceEndpoint = AUTH_SERVICE_URL + "/api/v1/auth/signin";

        HttpPost request = new HttpPost(serviceEndpoint);
        request.setHeader("Content-Type", "application/json");

        StringEntity entity = null;
        try {
            entity = new StringEntity(mapper.convertSigninRequestToJson(signInRequest));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            logger.info("HTTP Status Code: {}", response.getStatusLine().getStatusCode());
            return mapper.convertToJwtObject(
                    EntityUtils.toString(response.getEntity())
            );
        } catch (IOException e) {
            throw new AuthorizationException("Incorrect email or password");
        }
    }

    public boolean validateTokenClient(String jwt) {
        final String serviceEndpoint = AUTH_SERVICE_URL + "/api/v1/auth/validate-token";

        HttpGet request = new HttpGet(serviceEndpoint);
        request.addHeader("Authorization", "Bearer " + jwt);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode == 200;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    void destroy() throws IOException {
        httpClient.close();
    }

    public Response shortenUrlClient(UrlRequest urlRequest) {
        final String serviceEndpoint = API_HANDLER_SERVICE_URL + "/api/v1/data/shorten";

        HttpPost request = new HttpPost(serviceEndpoint);
        request.setHeader("Content-Type", "application/json");

        StringEntity entity = null;
        try {
            entity = new StringEntity(mapper.convertUrlRequestToJson(urlRequest));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("HTTP Status Code: {}", response.getStatusLine().getStatusCode());
            return mapper.convertToResponse(
                    EntityUtils.toString(response.getEntity())
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String redirectClient(String token) {
        final String serviceEndpoint = API_HANDLER_SERVICE_URL + "/api/v1/data/" + token;
        HttpGet request = new HttpGet(serviceEndpoint);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            logger.info("HTTP Status Code: {}", statusCode);

            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
