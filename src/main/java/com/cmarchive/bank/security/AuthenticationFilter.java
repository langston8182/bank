package com.cmarchive.bank.security;

import com.cmarchive.bank.exceptions.UserAuthenticationException;
import com.cmarchive.bank.service.TokenAuthenticationService;
import com.cmarchive.bank.service.TokenService;
import com.cmarchive.bank.utils.SpringBeanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private TokenAuthenticationService tokenAuthenticationService;
    private TokenService tokenService;
    private ModelMapper modelMapper;

    public AuthenticationFilter() {
        tokenAuthenticationService = SpringBeanUtils.getBean(TokenAuthenticationService.class);
        tokenService = SpringBeanUtils.getBean(TokenService.class);
        modelMapper = SpringBeanUtils.getBean(ModelMapper.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (httpRequest.getRequestURI().equals("/login")) {
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = tokenAuthenticationService.getAuthentication(httpRequest, httpResponse);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (authentication.isAuthenticated()) {
            chain.doFilter(request, response);
        } else {
            throw new UserAuthenticationException("Failed to authenticate");
        }
    }
}
