package com.cmarchive.bank.config;

import com.cmarchive.bank.component.MyUserDetails;
import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.service.TokenService;
import com.cmarchive.bank.utils.SpringBeanUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends GenericFilterBean {

    private TokenService tokenService;
    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationTokenFilter(TokenService tokenService, ModelMapper modelMapper) {
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
    }

    public AuthenticationTokenFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String token = getToken(httpRequest);

        if (token != null) {
            tokenService = SpringBeanUtils.getBean(TokenService.class);
            modelMapper = SpringBeanUtils.getBean(ModelMapper.class);
            if (tokenService != null && modelMapper != null) {
                User user = tokenService.findUserByAccessToken(token);
                MyUserDetails userDetails = modelMapper.map(user, MyUserDetails.class);

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        } else {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (username != null && password != null) {
                UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
                Authentication authentication = authenticationManager.authenticate(authReq);
            }

            System.out.println();
        }

        if (token != null) {
            request.setAttribute("x-auth-token", token);
        }
        chain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("x-auth-token");

        if (token == null) {
            token = request.getParameter("token");
        }

        return token;
    }
}
