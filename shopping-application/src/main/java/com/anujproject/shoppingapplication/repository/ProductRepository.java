package com.anujproject.shoppingapplication.repository;

import com.anujproject.shoppingapplication.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product,String> {
}
