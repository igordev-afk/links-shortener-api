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

            logger.info("Trying to connect to '{}'", url);
            logger.info("HTTP Status Code: {}", statusCode);

            if (statusCode >= 200 && statusCode < 300) {
                logger.info("Connection successful");
                return true;
            } else {
                logger.warn("Connection unsuccessful");
                return false;
            }
        } catch (IOException e) {
            logger.error("Error connecting to '{}': {}", url, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String generateShortLink(UrlRequest urlRequest) throws IOException {
        String shortenUrlEndpoint = shortlinkServiceUrl + "/api/shorten";
        HttpPost request = new HttpPost(shortenUrlEndpoint);
        request.setHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity("{\"longUrl\": \"" + urlRequest.getLongUrl() + "\"}");
        request.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();

            logger.info("Generating short link for '{}'", urlRequest.getLongUrl());
            logger.info("HTTP Status Code: {}", statusCode);

            if (statusCode >= 200 && statusCode < 300) {
                String shortLink = EntityUtils.toString(response.getEntity());
                logger.info("Short link generated successfully: '{}'", shortLink);
                return shortLink;
            } else {
                logger.error("Failed to generate short link");
                throw new RuntimeException();
            }
        } catch (IOException e) {
            logger.error("Error generating short link for '{}': {}", urlRequest.getLongUrl(), e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @PreDestroy
    void destroy() throws IOException {
        logger.info("Closing HTTP client");
        httpClient.close();
    }
}
