package com.cmarchive.bank.service;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.AuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenAuthenticationService {

    Authentication getAuthenticationForLogin(AuthenticationManager authManager, HttpServletRequest request, HttpServletResponse response);

    Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response);

    AuthenticationToken addAuthentication(HttpServletResponse response, MyUserDetails userDetails);

}
