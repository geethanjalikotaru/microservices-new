package com.example.mongodbdemo.repository;

import com.example.mongodbdemo.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface ProductRepository extends MongoRepository<Product,String> {
}
