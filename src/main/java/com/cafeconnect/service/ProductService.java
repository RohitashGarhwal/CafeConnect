package com.cafeconnect.service;

import com.cafeconnect.Model.AddProductRequest;
import com.cafeconnect.Model.UpdateProductStatusRequest;
import com.cafeconnect.Model.UpdateProductRequest;
import com.cafeconnect.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    ResponseEntity<String> addNewProduct(AddProductRequest addProductRequest);

    ResponseEntity<List<ProductWrapper>> getAllProduct();

    ResponseEntity<String> updateProduct(UpdateProductRequest updateProductRequest);

    ResponseEntity<String> deleteProduct(Integer id);

    ResponseEntity<String> updateStatus(UpdateProductStatusRequest updateProductStatusRequest);

    ResponseEntity<List<ProductWrapper>> getByCategory(Integer id);

    ResponseEntity<ProductWrapper> getProductById(Integer id);
}
