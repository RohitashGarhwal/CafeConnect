package com.cafeconnect.Model;


import lombok.Data;

@Data
public class GenerateReportRequest {

    private Boolean isGenerate;
    private String uuid;
    private String name;
    private String contactNumber;
    private String email;
    private String paymentMethod;
    private String productDetails;
    private Integer total;
}
