package com.tasc.apigateway.security;


import com.tasc.apigateway.model.TassUserAuthentication;
import com.tasc.apigateway.utils.HttpUtil;
import com.tasc.model.constans.AUTHENTICATION;

import com.tasc.redis.dto.UserLoginDTO;
import com.tasc.redis.repository.UserLoginRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class Oauth2AuthorizationFilter extends BasicAuthenticationFilter {

    UserLoginRepository userLoginRepository;

    public Oauth2AuthorizationFilter(
            AuthenticationManager authenticationManager, UserLoginRepository userLoginRepository) {
        super(authenticationManager);

        this.userLoginRepository = userLoginRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        // lay ra token


        String token = HttpUtil.getValueFromHeader(request, AUTHENTICATION.HEADER.TOKEN);

        if (StringUtils.isBlank(token)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Optional<UserLoginDTO> userLoginDTO = userLoginRepository.findById(token);

        if (userLoginDTO.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        UserLoginDTO userLoginDTOObject = userLoginDTO.get();

        List<String> roles = userLoginDTOObject.getRoles();

        String requestURI = request.getRequestURI();

        String method = request.getMethod();

        AntPathMatcher adt = new AntPathMatcher();

        for (String role : roles) {
            if (role.equalsIgnoreCase("SUPER_ADMIN")) {
                break;
            } else {
                for (String uri : userLoginDTOObject.getUris()) {
                    if (adt.match(uri, requestURI)) {
                        if (userLoginDTOObject.getMethods().contains(method)) {
                            break;
                        } else {
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            return;
                        }
                    }
                }
            }
        }

        UserDetailExtend userDetailExtend = new UserDetailExtend(userLoginDTOObject);

        TassUserAuthentication tassUserAuthentication = new TassUserAuthentication(userDetailExtend);

        SecurityContextHolder.getContext().setAuthentication(tassUserAuthentication);

        chain.doFilter(request, response);
    }
}
