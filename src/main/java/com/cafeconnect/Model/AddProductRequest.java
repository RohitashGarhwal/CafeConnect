package com.cafeconnect.Model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddProductRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, message = "name should have at least 2 character")
    private String name;

    @NotBlank(message = "Price is mandatory")
    private Integer price;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "status is mandatory")
    private String status;

    @NotBlank(message = "categoryId is mandatory")
    private Integer categoryId;

}
