package com.cafeconnect.restImpl;

import com.cafeconnect.Model.AddCategoryRequest;
import com.cafeconnect.Model.UpdateCategoryRequest;
import com.cafeconnect.POJO.Category;
import com.cafeconnect.constents.CafeConstants;
import com.cafeconnect.rest.CategoryRest;
import com.cafeconnect.service.CategoryService;
import com.cafeconnect.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryService categoryService;
    @Override
    public ResponseEntity<String> addNewCategory(AddCategoryRequest addCategoryRequest) {
        try {
            return categoryService.addNewCategory(addCategoryRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        try {
            return categoryService.updateCategory(updateCategoryRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
