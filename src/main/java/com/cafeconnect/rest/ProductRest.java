package com.cafeconnect.rest;

import com.cafeconnect.Model.AddProductRequest;
import com.cafeconnect.Model.UpdateProductStatusRequest;
import com.cafeconnect.Model.UpdateProductRequest;
import com.cafeconnect.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/product")
public interface ProductRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewProduct(@RequestBody AddProductRequest addProductRequest);

    @GetMapping(path = "/get")
    ResponseEntity<List<ProductWrapper>> getAllProduct();

    @PostMapping(path = "/update")
    ResponseEntity<String> updateProduct(@RequestBody UpdateProductRequest updateProductRequest);

    @PostMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    @PostMapping(path = "/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody UpdateProductStatusRequest updateProductStatusRequest);

    @GetMapping(path = "/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id);

    @GetMapping(path = "getById/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id);
}
