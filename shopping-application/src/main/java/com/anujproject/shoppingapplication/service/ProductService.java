package com.anujproject.shoppingapplication.service;

import com.anujproject.shoppingapplication.dto.ProductRequest;
import com.anujproject.shoppingapplication.dto.ProductResponse;
import com.anujproject.shoppingapplication.model.Product;
import com.anujproject.shoppingapplication.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder().
                name(productRequest.getName()).
                description(productRequest.getDescription()).
                price(productRequest.getPrice()).
                build();

        productRepository.save(product);
    }

    public List<ProductResponse> getProducts(){
        List<Product> products = productRepository.findAll();

      return  products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product)
    {
        return ProductResponse.builder().id(product.getId()).
                name(product.getName()).
                description(product.getDescription()).
                price(product.getPrice()).
                build();
    }

}
