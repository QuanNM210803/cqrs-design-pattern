package com.project.controller;

import com.project.dto.ProductRequest;
import com.project.entity.Product;
import com.project.service.ProductCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductCommandController {

    private final ProductCommandService commandService;

    @GetMapping("/{id}")
    public Product fetchProduct(@PathVariable long id) throws Exception {
        return commandService.getProduct(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return commandService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable long id, @RequestBody Product product) {
        return commandService.updateProduct(id, product);
    }

    @PostMapping("/products")
    public List<Product> createListProduct(@RequestBody ProductRequest products) {
        return commandService.createListProduct(products.getProducts());
    }
}
