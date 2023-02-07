package com.tasc.user.model.request.uri.update;

import lombok.Data;

import java.util.List;

@Data
public class UpdateUriRoleRequest {

    private Long uriId;
    private List<Long> roleIds;
}
