package ru.wwerlosh.configs;

import jakarta.annotation.PreDestroy;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import ru.wwerlosh.controllers.dto.UrlRequest;

import java.io.IOException;

@Configuration
public class HttpClient {

    private final static Logger logger = LoggerFactory.getLogger(HttpClient.class);

    private final static CloseableHttpClient httpClient = HttpClients.createDefault();

    @Value("${shortlink.service.url}")
    private String shortlinkServiceUrl;

    public boolean tryConnect(String url) throws IOException {
        HttpHead request = new HttpHead(url);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();

            logger.info("HTTP Status Code: {}", response.getStatusLine().getStatusCode());
            return statusCode >= 200 && statusCode < 300;
        }
    }

    public String generateShortLink(UrlRequest urlRequest) throws IOException {
        String shortenUrlEndpoint = shortlinkServiceUrl + "/api/shorten";
        HttpPost request = new HttpPost(shortenUrlEndpoint);
        request.setHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity("{\"longUrl\": \"" + urlRequest.getLongUrl() + "\"}");
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            logger.info("HTTP Status Code: {}", response.getStatusLine().getStatusCode());
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @PreDestroy
    void destroy() throws IOException {
        httpClient.close();
    }
}
