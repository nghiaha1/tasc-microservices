package com.tasc.user.model.request.user;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;

    private String password;

    private String confirmPassword;
}
