package com.tasc.user.service;

import com.tasc.entity.BaseStatus;
import com.tasc.user.aop.ApplicationException;
import com.tasc.model.BaseResponse;
import com.tasc.model.ERROR;
import com.tasc.user.entity.Role;
import com.tasc.user.model.request.role.create.CreateRoleRequest;
import com.tasc.user.model.request.role.update.UpdateRoleMethodRequest;
import com.tasc.user.model.request.role.update.UpdateRoleUriRequest;
import com.tasc.user.repository.MethodRepository;
import com.tasc.user.repository.RoleRepository;
import com.tasc.user.repository.UriRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MethodRepository methodRepository;

    @Autowired
    private UriRepository uriRepository;

    public BaseResponse<Role> create(CreateRoleRequest request) throws ApplicationException {
        log.info("1 - Create role request: {}", request);
        validationCreateRequest(request);

        log.info("2 - Create role");
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setStatus(BaseStatus.ACTIVE);

        log.info("3 - Save role: {}", role);
        roleRepository.save(role);

        return new BaseResponse<>(role);
    }

    public BaseResponse<Role> updateRoleMethod(UpdateRoleMethodRequest request) throws ApplicationException {
        log.info("1 - Create update role method request: {}", request);
        validationUpdateRoleMethodRequest(request);

        log.info("2 - Get role by ID: {}", request.getRoleId());
        Role role = roleRepository.findById(request.getRoleId()).get();
        role.setMethods(methodRepository.findAllById(request.getMethodIds()));

        log.info("3 - Save role: {}", role);
        roleRepository.save(role);

        return new BaseResponse<>(role);
    }

    public BaseResponse<Role> updateRoleUri(UpdateRoleUriRequest request) throws ApplicationException {
        log.info("1 - Create update role uri request: {}", request);
        validationUpdateRoleUriRequest(request);

        log.info("2 - Get role by ID: {}", request.getRoleId());
        Role role = roleRepository.findById(request.getRoleId()).get();
        role.setUris(uriRepository.findAllById(request.getUriIds()));

        log.info("3 - Save role: {}", role);
        roleRepository.save(role);

        return new BaseResponse<>(role);
    }

    public BaseResponse<Page<Role>> findAllByStatus(BaseStatus status, int page, int pageSize) throws ApplicationException {
        log.info("1 - Find all role");
        Page<Role> roles = roleRepository.findByStatus(status, PageRequest.of(page, pageSize));

        return new BaseResponse<>(roles);
    }

    private void validationCreateRequest(CreateRoleRequest request) throws ApplicationException {
        log.info("1.1 - Validation create role request name: {}", request.getName());
        if (StringUtils.isBlank(request.getName())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role name is required");
        }

        log.info("1.2 - Validation create role request description: {}", request.getDescription());
        if (StringUtils.isBlank(request.getDescription())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role description is required");
        }

        log.info("1.3 - Validation create role request name is existed: {}", request.getName());
        if (roleRepository.findByName(request.getName()).isPresent()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role name is existed");
        }
    }

    private void validationUpdateRoleMethodRequest(UpdateRoleMethodRequest request) throws ApplicationException {
        log.info("1.1 - Validation update role method request role ID is null");
        if (request.getRoleId() == null || request.getRoleId() == 0) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role ID is required");
        }

        log.info("1.2 - Validation update role method request role ID: {}", request.getRoleId());
        if (roleRepository.findById(request.getRoleId()).isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role ID is not existed");
        }

        log.info("1.3 - Validation update role method request method ID is null");
        if (request.getMethodIds() == null || request.getMethodIds().isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Method ID is required");
        }

        log.info("1.4 - Validation update role method request method ID: {}", request.getMethodIds());
        if (methodRepository.findByIdIn(request.getMethodIds()).size() != request.getMethodIds().size()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Method ID is not existed");
        }
    }

    private void validationUpdateRoleUriRequest(UpdateRoleUriRequest request) throws ApplicationException {
        log.info("1.1 - Validation update role uri request role ID is null");
        if (request.getRoleId() == null || request.getRoleId() == 0) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role ID is required");
        }

        log.info("1.2 - Validation update role uri request role ID: {}", request.getRoleId());
        if (roleRepository.existsById(request.getRoleId())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role ID is not existed");
        }

        log.info("1.3 - Validation update role uri request uri ID is null");
        if (request.getUriIds() == null || request.getUriIds().isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Uri ID is required");
        }

        log.info("1.4 - Validation update role uri request uri ID: {}", request.getUriIds());
        if (uriRepository.findByIdIn(request.getUriIds()).size() != request.getUriIds().size()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Uri ID is not existed");
        }
    }
}
