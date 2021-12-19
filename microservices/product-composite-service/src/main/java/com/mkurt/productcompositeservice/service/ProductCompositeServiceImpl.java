package com.mkurt.productcompositeservice.service;

import com.mkurt.api.composite.*;
import com.mkurt.api.core.product.Product;
import com.mkurt.api.core.recommendation.Recommendation;
import com.mkurt.api.core.review.Review;
import com.mkurt.api.exception.NotFoundException;
import com.mkurt.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositionService {

    private final ServiceUtil serviceUtil;
    private ProductCompositeIntegration integration;

    @Autowired
    public ProductCompositeServiceImpl(
            ServiceUtil serviceUtil,
            ProductCompositeIntegration integration) {

        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }


    @Override
    public ProductAggregate getProductAggregate(int productId) {

        Product product = integration.getProduct(productId);

        if(product == null)
            throw new NotFoundException("No product found for productId: " + productId);

        List<Recommendation> recommendations = integration.getRecommendations(productId);
        List<Review> reviews = integration.getReviews(productId);

        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    private ProductAggregate createProductAggregate(Product product,
                                                    List<Recommendation> recommendations,
                                                    List<Review> reviews,
                                                    String serviceAddress) {
        // setup product info
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // Copy summary recommendation info, if available
        List<RecommendationSummary> recommendationSummaries =
                (recommendations == null) ? null : recommendations.stream()
                        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
                        .collect(Collectors.toList());

        // Copy summary review info, if available
        List<ReviewSummary> reviewSummaries =
                (reviews == null) ? null : reviews.stream()
                        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
                        .collect(Collectors.toList());


        String productAddress = product.getServiceAddress();
        String reviewAddress = (reviews != null && reviews.size() > 0) ? reviews.get(0).getServiceAddress() : "";
        String recommendationAddress = (recommendations != null && recommendations.size() > 0) ? recommendations.get(0).getServiceAddress() : "";
        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
    }

}
