package com.cafeconnect.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupRequest {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, message = "name should have at least 2 character")
    private String name;
    @Size(min = 10, max = 10, message = "contact number should be valid")
    private String contactNumber;

    @Email(message = "email should be valid")
    private String email;
    private String password;
}
