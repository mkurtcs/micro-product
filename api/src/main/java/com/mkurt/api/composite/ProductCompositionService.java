package com.mkurt.api.composite;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductCompositionService {


    @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
    ProductAggregate getProductAggregate(@PathVariable int productId);
}

