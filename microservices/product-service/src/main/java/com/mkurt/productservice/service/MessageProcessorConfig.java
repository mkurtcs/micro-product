package com.mkurt.productservice.service;

import com.mkurt.api.core.product.Product;
import com.mkurt.api.core.product.ProductService;
import com.mkurt.api.event.Event;
import com.mkurt.api.exception.EventProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {

    private final static Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final ProductService productService;

    @Autowired
    public MessageProcessorConfig(ProductService productService) {
        this.productService = productService;
    }


    /**
     * To ensure that we can propagate exceptions thrown by the productService bean back to the messaging system,
     * we call the block() method on the responses we get back from the productService bean. This ensures that
     * the message processor waits for the productService bean to complete its creation or deletion in the
     * underlying database. Without calling the block() method, we would not be able to propagate exceptions and
     * the messaging system would not be able to re-queue a failed attempt or possibly move the message
     * to a dead-letter queue; instead, the message would silently be dropped.
     */
    @Bean
    public Consumer<Event<Integer, Product>> messageProcessor() {

        return event -> {
            LOG.info("Process message created at {}...", event.getEventCreatedAt());

            switch (event.getEventType()) {

                case CREATE:
                    Product product = event.getData();
                    LOG.info("Create product with ID: {}", product.getProductId());
                    productService.createProduct(product)
                            .block();
                    break;

                case DELETE:
                    int productId = event.getKey();
                    LOG.info("Delete recommendations with ProductID: {}", productId);
                    productService.deleteProduct(productId)
                            .block();
                    break;

                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected a CREATE or DELETE event";
                    LOG.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }
            LOG.info("Message processing done!");
        };
    }

}
