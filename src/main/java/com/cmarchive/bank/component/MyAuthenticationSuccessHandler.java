package com.cmarchive.bank.component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.cmarchive.bank.domain.User;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private LoggedUser loggedUser = new LoggedUser();

	@Bean
	public LoggedUser loggedUser() {
		return loggedUser;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		if (auth.getPrincipal() instanceof User) {
			User user = (User) auth.getPrincipal();
			loggedUser().setUser(user);

			response.sendRedirect("/operations/list");
		}
	}
}
