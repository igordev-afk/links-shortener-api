package ru.wwerlosh.configs;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import ru.wwerlosh.dao.request.SignInRequest;
import ru.wwerlosh.dao.request.SignUpRequest;
import ru.wwerlosh.dao.response.JwtAuthenticationResponse;
import ru.wwerlosh.utils.DtoMapper;

@Configuration
public class HttpClient {

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private final DtoMapper mapper;
    private final static CloseableHttpClient httpClient = HttpClients.createDefault();

    public HttpClient(DtoMapper mapper) {
        this.mapper = mapper;
    }

    public JwtAuthenticationResponse signUpClient(SignUpRequest signUpRequest) {
        final String serviceEndpoint = "http://localhost:8083/api/v1/auth/signup";

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
            throw new RuntimeException(e);
        }
    }

    public JwtAuthenticationResponse signInClient(SignInRequest signInRequest) {
        final String serviceEndpoint = "http://localhost:8083/api/v1/auth/signin";

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
            throw new RuntimeException(e);
        }
    }

    @PreDestroy
    void destroy() throws IOException {
        httpClient.close();
    }
}
