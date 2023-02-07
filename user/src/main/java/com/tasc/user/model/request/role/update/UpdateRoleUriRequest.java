package com.tasc.user.model.request.role.update;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRoleUriRequest {

        private Long roleId;

        private List<Long> uriIds;
}
