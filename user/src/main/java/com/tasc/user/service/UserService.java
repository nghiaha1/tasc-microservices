package com.tasc.user.service;

import com.tasc.entity.BaseStatus;
import com.tasc.user.aop.ApplicationException;
import com.tasc.model.BaseResponse;
import com.tasc.model.ERROR;
import com.tasc.redis.dto.UserLoginDTO;
import com.tasc.redis.repository.UserLoginRepository;
import com.tasc.user.entity.Method;
import com.tasc.user.entity.Role;
import com.tasc.user.entity.Uri;
import com.tasc.user.entity.User;
import com.tasc.user.model.request.user.LoginRequest;
import com.tasc.user.model.request.user.RegisterRequest;
import com.tasc.user.model.request.user.UpdateUserRoleRequest;
import com.tasc.user.repository.RoleRepository;
import com.tasc.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class UserService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public BaseResponse<User> register(RegisterRequest request) throws ApplicationException {
        log.info("1 - Register request: {}", request);
        validationRegisterRequest(request);

        List<Role> roles = new ArrayList<>();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setStatus(BaseStatus.ACTIVE);

        roles.add(roleRepository.findByName("DEFAULT_ROLE").get());
        user.setRoles(roles);

        log.info("2 - Save user: {}", user);
        userRepository.save(user);

        return new BaseResponse<>(user);
    }

    public BaseResponse<UserLoginDTO> login(LoginRequest request) throws ApplicationException {
        log.info("1 - Login request: {}", request);
        validationLoginRequest(request);

        log.info("2 - Find user by username: {}", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername());

        log.info("3 - Check user is null");
        if (user == null) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username or password is incorrect");
        }

        log.info("4 - Check user status");
        if (user.getStatus() != BaseStatus.ACTIVE) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "User is not active");
        }

        log.info("5 - Check password");
        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username or password is incorrect");
        }

        log.info("6 - Create userLoginDTO user to redis");
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setUserId(user.getId());
        userLoginDTO.setToken(UUID.randomUUID().toString());
        userLoginDTO.setTimeToLive(1);

        List<String> roleList = new ArrayList<>();
        List<String> uriList = new ArrayList<>();
        List<String> methodList = new ArrayList<>();

        for (Role role : user.getRoles()) {
            Optional<Role> optionalRole = roleRepository.findById(role.getId());
            if (optionalRole.isPresent()) {
                roleList.add(optionalRole.get().getName());

                for (Uri uri : optionalRole.get().getUris()) {
                    uriList.add(uri.getUri());
                }

                for (Method method : optionalRole.get().getMethods()) {
                    methodList.add(method.getMethod());
                }
            }
        }

        userLoginDTO.setRoles(roleList);
        userLoginDTO.setUris(uriList);
        userLoginDTO.setMethods(methodList);

        log.info("7 - Save user to redis: {}", userLoginDTO);
        userLoginRepository.save(userLoginDTO);

        return new BaseResponse<>(userLoginDTO);
    }

    public BaseResponse<User> updateUserRole(UpdateUserRoleRequest request) throws ApplicationException {
        log.info("1 - Update user role request: {}", request);
        validationUpdateUserRoleRequest(request);

        log.info("2 - Find user by id: {}", request.getUserId());
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            log.info("2.1 - User is empty");
            throw new ApplicationException(ERROR.INVALID_PARAM, "User is not exist");
        }

        log.info("3 - Find role by id: {}", request.getRoleIds());

        log.info("4 - Get existing roles");
        List<Role> existingRoles = optionalUser.get().getRoles();
        log.info("4.1 - Existing roles: {}", existingRoles);

        log.info("5 - Get role list from request with IDs: {}", request.getRoleIds());
        List<Role> roleList = roleRepository.findAllById(request.getRoleIds());

        if (roleList.isEmpty()) {
            log.info("5.1 - Role list is empty");
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role is not exist");
        }

        log.info("6 - Check existing roles is empty");
        for (Role role : existingRoles) {
            if (!roleList.contains(role)) {
                roleList.add(role);
            }
        }

        log.info("7 - Update user role with role list: {}", roleList);
        optionalUser.get().setRoles(roleList);

        log.info("8 - Save user: {}", optionalUser.get());
        userRepository.save(optionalUser.get());

        return new BaseResponse<>(optionalUser.get());
    }

    private void validationRegisterRequest(RegisterRequest request) throws ApplicationException {
        log.info("1.1 - Validation register request username: {}", request.getUsername());
        if (StringUtils.isBlank(request.getUsername())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username is required");
        }

        log.info("1.2 - Validation register request username is exist: {}", request.getUsername());
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username is already exist");
        }

        log.info("1.3 - Validation register request password: {}", request.getPassword());
        if (StringUtils.isBlank(request.getPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password is required");
        }

        log.info("1.4 - Validation register request confirm password: {}", request.getConfirmPassword());
        if (StringUtils.isBlank(request.getConfirmPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Confirm password is required");
        }

        log.info("1.5 - Validation register request password and confirm password with password: {}", request.getPassword());
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password and confirm password are not match");
        }
    }

    private void validationLoginRequest(LoginRequest request) throws ApplicationException {
        log.info("1.1 - Validation login request username: {}", request.getUsername());
        if (StringUtils.isBlank(request.getUsername())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Username is required");
        }

        log.info("1.2 - Validation login request password: {}", request.getPassword());
        if (StringUtils.isBlank(request.getPassword())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Password is required");
        }
    }

    private void validationUpdateUserRoleRequest(UpdateUserRoleRequest request) throws ApplicationException {
        log.info("1.1 - Validation update user role request user id: {}", request.getUserId());
        if (request.getUserId() == null || request.getUserId() <= 0) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "User id is required");
        }

        log.info("1.2 - Validation update user role request user id is exist: {}", request.getUserId());
        if (!userRepository.existsById(request.getUserId())) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "User id is not exist");
        }

        log.info("1.3 - Validation update user role request role id: {}", request.getRoleIds());
        if (request.getRoleIds() == null || request.getRoleIds().isEmpty()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role id is required");
        }

        log.info("1.4 - Validation update user role request role id is exist: {}", request.getRoleIds());
        if (roleRepository.findByIdIn(request.getRoleIds()).size() != request.getRoleIds().size()) {
            throw new ApplicationException(ERROR.INVALID_PARAM, "Role id is not exist");
        }
    }
}
