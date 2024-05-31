package com.example.JsonView.services;

import com.example.JsonView.entities.Product;
import com.example.JsonView.repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class GenerateListProducts {
    private final ProductRepository productRepository;
    private static final Random random = new Random();

    public GenerateListProducts(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> generateListOfProducts() {
        List<Product> allProducts = productRepository.findAll();
        int count = random.nextInt(1,10);
        List<Product> listProducts = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            listProducts.add(allProducts.get(i));
        }
        return listProducts;
    }
}
