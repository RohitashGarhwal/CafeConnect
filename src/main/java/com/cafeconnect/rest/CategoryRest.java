package com.cafeconnect.rest;


import com.cafeconnect.Model.AddCategoryRequest;
import com.cafeconnect.Model.UpdateCategoryRequest;
import com.cafeconnect.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/category")
public interface CategoryRest {

    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) AddCategoryRequest addCategoryRequest);

    @GetMapping(path = "/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody UpdateCategoryRequest updateCategoryRequest);
}
