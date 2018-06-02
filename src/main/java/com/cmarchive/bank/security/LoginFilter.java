package com.cmarchive.bank.security;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.AuthenticationToken;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.exceptions.UserAuthenticationException;
import com.cmarchive.bank.service.TokenAuthenticationService;
import com.cmarchive.bank.service.TokenService;
import com.cmarchive.bank.utils.SpringBeanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private TokenAuthenticationService tokenAuthenticationService;
    private TokenService tokenService;
    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;

    public LoginFilter(AuthenticationManager authenticationManager, RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);

        tokenAuthenticationService = SpringBeanUtils.getBean(TokenAuthenticationService.class);
        tokenService = SpringBeanUtils.getBean(TokenService.class);
        modelMapper = SpringBeanUtils.getBean(ModelMapper.class);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username != null && password != null) {
            super.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        Authentication authentication = tokenAuthenticationService.getAuthenticationForLogin(authenticationManager, request, response);

        if (authentication != null && !authentication.isAuthenticated()) {
            throw new UserAuthenticationException("Authentication failed");
        }

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        tokenAuthenticationService.addAuthentication(response, (MyUserDetails) authResult.getPrincipal()).getToken();
        SecurityContextHolder.getContext().setAuthentication(authResult);

        super.successfulAuthentication(request, response, chain, authResult);
    }

}