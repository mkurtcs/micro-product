package com.mkurt.productcompositeservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mkurt.api.core.product.Product;
import com.mkurt.api.core.product.ProductService;
import com.mkurt.api.core.recommendation.Recommendation;
import com.mkurt.api.core.recommendation.RecommendationService;
import com.mkurt.api.core.review.Review;
import com.mkurt.api.core.review.ReviewService;
import com.mkurt.api.exception.InvalidInputException;
import com.mkurt.api.exception.NotFoundException;
import com.mkurt.util.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Component
public class ProductCompositeIntegration implements ProductService, ReviewService, RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final String productServiceUrl;
    private final String reviewServiceUrl;
    private final String recommendationServiceUrl;

    public ProductCompositeIntegration(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") String productServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") String reviewServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") String recommendationServicePort) {
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        this.reviewServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";
        this.recommendationServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    @Override
    public Product getProduct(int productId) {

        String requestUrl = productServiceUrl + productId;
        LOG.debug("Will call getProduct API on URL: {}", requestUrl);


        try {
            Product product = restTemplate.getForObject(requestUrl, Product.class);
            LOG.debug("Found a product with id: {}", product.getProductId());
            return product;
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                case NOT_FOUND:
                    throw new NotFoundException(getErrorMessage(e));
                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(getErrorMessage(e));
                default:
                    LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", e.getStatusCode());
                    LOG.warn("Error body: {}", e.getResponseBodyAsString());
                    throw e;
            }
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {

        try {
            String url = recommendationServiceUrl + productId;

            LOG.debug("Will call getRecommendations API on URL: {}", url);
            List<Recommendation> recommendations = restTemplate
                    .exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {})
                    .getBody();

            LOG.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
            return recommendations;

        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting recommendations, return zero recommendations: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public List<Review> getReviews(int productId) {

        try {
            String url = reviewServiceUrl + productId;

            LOG.debug("Will call getReviews API on URL: {}", url);
            List<Review> reviews = restTemplate
                    .exchange(url, GET, null, new ParameterizedTypeReference<List<Review>>() {})
                    .getBody();

            LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
            return reviews;

        } catch (Exception ex) {
            LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

}
