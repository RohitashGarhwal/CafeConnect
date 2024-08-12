package com.cafeconnect.Model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    @Email
    private String email;
}
