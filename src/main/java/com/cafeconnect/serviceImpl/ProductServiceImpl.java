package com.cafeconnect.serviceImpl;

import com.cafeconnect.JWT.JwtFilter;
import com.cafeconnect.Model.AddProductRequest;
import com.cafeconnect.Model.UpdateProductStatusRequest;
import com.cafeconnect.Model.UpdateProductRequest;
import com.cafeconnect.POJO.Category;
import com.cafeconnect.POJO.Product;
import com.cafeconnect.constents.CafeConstants;
import com.cafeconnect.dao.CategoryDao;
import com.cafeconnect.dao.ProductDao;
import com.cafeconnect.service.ProductService;
import com.cafeconnect.utils.CafeUtils;
import com.cafeconnect.wrapper.ProductWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {


    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ProductDao productDao;


    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewProduct(AddProductRequest addProductRequest) {
        try {
            if(jwtFilter.isAdmin()) {
                productDao.save(convertToProductEntity(addProductRequest));
                return CafeUtils.getResponseEntity("Product added successfully", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private Product convertToProductEntity(AddProductRequest addProductRequest) {
        Product product = new Product();
        Category category = new Category();
        category.setId(addProductRequest.getCategoryId());
        product.setCategory(category);
        product.setName(addProductRequest.getName());
        product.setPrice(addProductRequest.getPrice());
        product.setDescription(addProductRequest.getDescription());
        product.setStatus(addProductRequest.getStatus());
        return product;
    }


    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            return new ResponseEntity<>(productDao.getAllProduct(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(UpdateProductRequest updateProductRequest) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(updateProductRequest.getId());
                if (optional.isPresent()) {
                    Product product = optional.get();
                    updateProductEntity(product, updateProductRequest);
                    productDao.save(product);
                    return CafeUtils.getResponseEntity("Product Updated Successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Product id doesn't exist", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private void updateProductEntity(Product product, UpdateProductRequest updateProductRequest) {
        product.setName(updateProductRequest.getName());
        product.setPrice(updateProductRequest.getPrice());
        product.setDescription(updateProductRequest.getDescription());
        product.setStatus(updateProductRequest.getStatus());
    }



    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional optional = productDao.findById(id);
                if (optional != null) {
                    productDao.deleteById(id);
                    return CafeUtils.getResponseEntity("Product Deleted Successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product id doesn't exist.", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public ResponseEntity<String> updateStatus(UpdateProductStatusRequest updateProductStatusRequest) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<Product> optional = productDao.findById(updateProductStatusRequest.getId());
                if (optional.isPresent()) {
                    Product product = optional.get();
                    product.setStatus(updateProductStatusRequest.getStatus());
                    productDao.save(product);
                    return CafeUtils.getResponseEntity("Product status updated successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Product id doesn't exist", HttpStatus.OK);
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getByCategory(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductByCategory(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productDao.getProductById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}