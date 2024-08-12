package com.cafeconnect.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.xml.stream.XMLInputFactory;

@Data
public class UserLoginRequest {

    @Email(message = "email should be valid")
    private String email;
    private String password;
}
