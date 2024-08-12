package com.cafeconnect.service;

import com.cafeconnect.Model.AddCategoryRequest;
import com.cafeconnect.Model.UpdateCategoryRequest;
import com.cafeconnect.POJO.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<String> addNewCategory(AddCategoryRequest addCategoryRequest);

    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> updateCategory(UpdateCategoryRequest updateCategoryRequest);

}
