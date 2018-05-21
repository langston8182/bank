package com.cmarchive.bank.component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmarchive.bank.domain.AuthenticationToken;
import com.cmarchive.bank.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cmarchive.bank.domain.User;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private LoggedUser loggedUser = new LoggedUser();
	private TokenService tokenService;

	@Autowired
	public MyAuthenticationSuccessHandler(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Bean
	public LoggedUser loggedUser() {
		return loggedUser;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		if (auth.getPrincipal() instanceof MyUserDetails) {
			User user = ((MyUserDetails) auth.getPrincipal()).getUser();
			loggedUser().setUser(user);

			AuthenticationToken authToken = tokenService.generateToken(user);
			response.sendRedirect("/operations/list?token=" + authToken.getToken());
		}
	}
}
