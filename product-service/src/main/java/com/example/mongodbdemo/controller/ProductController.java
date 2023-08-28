package com.example.mongodbdemo.controller;

import com.example.mongodbdemo.dto.ProductRequest;
import com.example.mongodbdemo.dto.ProductResponse;
import com.example.mongodbdemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService ps;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest pr){
        ps.createProduct(pr);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return ps.getAllProducts();
    }
}
