package com.cafeconnect.serviceImpl;

import com.cafeconnect.JWT.JwtFilter;
import com.cafeconnect.Model.AddCategoryRequest;
import com.cafeconnect.Model.UpdateCategoryRequest;
import com.cafeconnect.POJO.Category;
import com.cafeconnect.constents.CafeConstants;
import com.cafeconnect.dao.CategoryDao;
import com.cafeconnect.service.CategoryService;
import com.cafeconnect.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(AddCategoryRequest addCategoryRequest) {
        try {
            if (jwtFilter.isAdmin()) {
                Category category = new Category();
                category.setName(addCategoryRequest.getName());
                categoryDao.save(category);
                return CafeUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    private Category convertToCategoryEntity(AddCategoryRequest addCategoryRequest) {
//        Category category = new Category();
//        category.setName(addCategoryRequest.getName());
//        return category;
//    }

//    private void updateCategoryEntity(Category category, UpdateCategoryRequest updateCategoryRequest) {
//        category.setName(updateCategoryRequest.getName());
//    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Category>>(categoryDao.findAll(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Category> optional = categoryDao.findById(updateCategoryRequest.getId());
                if (optional.isPresent()) {
                    Category category = optional.get();
                    category.setName(updateCategoryRequest.getName());
                    categoryDao.save(category);
                    return CafeUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Category id doesn't exist", HttpStatus.NOT_FOUND);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
