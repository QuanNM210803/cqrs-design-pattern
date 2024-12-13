package com.project.controller;

import com.project.entity.Product;
import com.project.service.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/products")
@RestController
@RequiredArgsConstructor
public class ProductQueryController {
    private final ProductQueryService queryService;

    @GetMapping
    public List<Product> fetchAllProducts(){
        return queryService.getProducts();
    }

    @GetMapping("/{id}")
    public Product fetchProduct(@PathVariable long id) throws Exception {
        return queryService.getProduct(id);
    }
}
