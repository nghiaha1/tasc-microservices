package com.tasc.user.model.request.method.update;

import lombok.Data;

import java.util.List;

@Data
public class UpdateMethodRoleRequest {
    private Long methodId;

    private List<Long> roleIds;
}