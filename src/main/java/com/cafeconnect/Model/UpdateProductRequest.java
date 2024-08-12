package com.cafeconnect.Model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {

    @Id
    @NotBlank(message = "id is mandatory")
    private Integer id;

    private String name;
    private Integer price;
    private String description;
    private String status;
}
