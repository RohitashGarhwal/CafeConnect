package com.cafeconnect.Model;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class UpdateProductStatusRequest {

    @Id
    private Integer id;
    private String status;
}
