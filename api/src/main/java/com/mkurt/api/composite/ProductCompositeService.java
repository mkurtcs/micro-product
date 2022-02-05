package com.mkurt.api.composite;


import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


public interface ProductCompositeService {


    @PostMapping(value    = "/product-composite", consumes = "application/json")
    Mono<Void> createCompositeProduct(@RequestBody ProductAggregate body);


    @GetMapping(value = "/product-composite/{productId}", produces = "application/json")
    Mono<ProductAggregate> getCompositeProduct(@PathVariable int productId);


    @DeleteMapping(value = "/product-composite/{productId}")
    Mono<Void> deleteCompositeProduct(@PathVariable int productId);
}

