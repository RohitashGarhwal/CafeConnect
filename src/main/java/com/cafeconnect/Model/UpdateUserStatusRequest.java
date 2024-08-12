package com.cafeconnect.Model;

import lombok.Data;

@Data
public class UpdateUserStatusRequest {
    private Integer id;
    private String status;
}
