package com.tasc.user.controller;

import com.tasc.entity.BaseStatus;
import com.tasc.user.aop.ApplicationException;
import com.tasc.model.BaseController;
import com.tasc.model.BaseResponse;
import com.tasc.user.model.request.role.create.CreateRoleRequest;
import com.tasc.user.model.request.role.update.UpdateRoleMethodRequest;
import com.tasc.user.model.request.role.update.UpdateRoleUriRequest;
import com.tasc.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    @PostMapping(path = "/create")
    public ResponseEntity<BaseResponse> create(@RequestBody CreateRoleRequest request) throws ApplicationException {
        return createdResponse(roleService.create(request));
    }

    @PutMapping(path = "/update-role-method")
    public ResponseEntity<BaseResponse> updateRoleMethod(@RequestBody UpdateRoleMethodRequest request) throws ApplicationException {
        return createdResponse(roleService.updateRoleMethod(request));
    }

    @PutMapping(path = "/update-role-uri")
    public ResponseEntity<BaseResponse> updateRoleUri(@RequestBody UpdateRoleUriRequest request) throws ApplicationException {
        return createdResponse(roleService.updateRoleUri(request));
    }

    @GetMapping(path = "/get-all/{status}/{page}/{pageSize}")
    public ResponseEntity<BaseResponse> getAll(@PathVariable String status,
                                               @PathVariable int page,
                                               @PathVariable int pageSize) throws ApplicationException {
        return createdResponse(roleService.findAllByStatus(BaseStatus.valueOf(status.toUpperCase()), page, pageSize));
    }
}
