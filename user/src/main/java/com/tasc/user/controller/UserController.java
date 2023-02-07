package com.tasc.user.controller;

import com.tasc.user.aop.ApplicationException;
import com.tasc.model.BaseController;
import com.tasc.model.BaseResponse;
import com.tasc.user.model.request.user.LoginRequest;
import com.tasc.user.model.request.user.RegisterRequest;
import com.tasc.user.model.request.user.UpdateUserRoleRequest;
import com.tasc.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest request) throws ApplicationException {
        return createdResponse(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest request) throws ApplicationException {
        return createdResponse(userService.login(request));
    }

    @PutMapping("/update-user-role")
    public ResponseEntity<BaseResponse> updateUserRole(@RequestBody UpdateUserRoleRequest request) throws ApplicationException {
        return createdResponse(userService.updateUserRole(request));
    }
}
