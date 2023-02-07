package com.tasc.user.model.request.user;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRoleRequest {

    private Long userId;
    private List<Long> roleIds;
}
