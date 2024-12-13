package com.project.service;

import com.project.dto.ProductEvent;
import com.project.entity.Product;
import com.project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCommandService {
    private final ProductRepository repository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Product getProduct(long id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new Exception("NOT FOUND"));
    }

    public Product createProduct(Product product) {
        Product productSave = repository.save(product);
        ProductEvent event = new ProductEvent("CREATE_PRODUCT", productSave);
        kafkaTemplate.send("product-event-topic", event);

        return productSave;
    }

    public List<Product> createListProduct(List<Product> products){
        List<Product> productSave=repository.saveAll(products);
        productSave.forEach(product -> {
                    ProductEvent event = new ProductEvent("CREATE_PRODUCT", product);
                    kafkaTemplate.send("product-event-topic", event);
                });
        return productSave;
    }

    public Product updateProduct(long id, Product product) {
        Product existingProduct = repository.findById(id).get();
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());

        Product productSave = repository.save(existingProduct);
        ProductEvent event = new ProductEvent("UPDATE_PRODUCT", productSave);
        kafkaTemplate.send("product-event-topic", event);

        return productSave;
    }
}
