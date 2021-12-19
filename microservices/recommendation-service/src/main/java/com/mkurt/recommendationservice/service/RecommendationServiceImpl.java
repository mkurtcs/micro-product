package com.mkurt.recommendationservice.service;

import com.mkurt.api.core.recommendation.Recommendation;
import com.mkurt.api.core.recommendation.RecommendationService;
import com.mkurt.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final ServiceUtil serviceUtil;

    public RecommendationServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }


    @Override
    public List<Recommendation> getRecommendations(int productId) {
        return null;
    }

}
