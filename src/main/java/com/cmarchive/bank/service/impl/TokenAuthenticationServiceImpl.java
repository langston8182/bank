package com.cmarchive.bank.service.impl;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.AuthenticationToken;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.service.TokenAuthenticationService;
import com.cmarchive.bank.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private Authentication authentication;
    private TokenService tokenService;

    @Autowired
    public TokenAuthenticationServiceImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication getAuthenticationForLogin(AuthenticationManager authManager, HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null && password == null) {
            return null;
        }

        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
        Authentication auth = authManager.authenticate(authReq);

        this.authentication = auth;
        return auth;
    }

    @Override
    public AuthenticationToken addAuthentication(HttpServletResponse response, MyUserDetails userDetails) {
        AuthenticationToken token = tokenService.generateToken(userDetails.getUser());

        response.addHeader("token", token.getToken());

        return token;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("token");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (token != null) {
            User user = tokenService.findUserByAccessToken(token);
            if (user != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                authentication.setAuthenticated(true);
                response.setHeader("token", token);

                return authentication;
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        authentication.setAuthenticated(false);
        return authentication;
    }
}
