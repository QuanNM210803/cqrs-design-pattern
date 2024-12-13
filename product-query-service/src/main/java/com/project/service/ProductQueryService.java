package com.project.service;

import com.project.dto.ProductEvent;
import com.project.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductQueryService {

    private static final String KEY = "PRODUCT:";
    private static final String HASH_KEY = "product";

    private final RedisTemplate<String, Object> redisTemplate;

    public List<Product> getProducts() {
        Set<String> keys = redisTemplate.keys(KEY + "*");
        return Objects.requireNonNull(keys).stream()
                .map(key -> (Product) redisTemplate.opsForHash().get(key, HASH_KEY))
                .toList();
    }

    public Product getProduct(long id) throws Exception {
        return (Product) redisTemplate.opsForHash().get(KEY + id, HASH_KEY);
    }

    @KafkaListener(topics = "product-event-topic", groupId = "product-event-group")
    public void processProductEvents(ProductEvent productEvent) {
        Product product = productEvent.getProduct();

        if (productEvent.getEventType().equals("CREATE_PRODUCT")) {
            redisTemplate.opsForHash().put(KEY + product.getId(), HASH_KEY, product);
        }
        if (productEvent.getEventType().equals("UPDATE_PRODUCT")) {
            Product existingProductInRedis = (Product) redisTemplate.opsForHash().get(KEY + product.getId(), HASH_KEY);
            if (!Objects.isNull(existingProductInRedis)) {
                existingProductInRedis.setName(product.getName());
                existingProductInRedis.setPrice(product.getPrice());
                existingProductInRedis.setDescription(product.getDescription());
                redisTemplate.opsForHash().put(KEY + product.getId(), HASH_KEY, existingProductInRedis);
            } else {
                redisTemplate.opsForHash().put(KEY + product.getId(), HASH_KEY, product);
            }
        }
    }
}
