package com.cafeconnect.Model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCategoryRequest {
    @Id
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, message = "name should have at least 2 character")
    private String name;
}
