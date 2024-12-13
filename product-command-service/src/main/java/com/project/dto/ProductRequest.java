package com.project.dto;

import com.project.entity.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    private List<Product> products;
}
