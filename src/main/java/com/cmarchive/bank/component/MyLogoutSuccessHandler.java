package com.cmarchive.bank.component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

	private LoggedUser loggedUser;

	@Autowired
	public MyLogoutSuccessHandler(LoggedUser loggedUser) {
		super();
		this.loggedUser = loggedUser;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {
		loggedUser.setUser(null);
		response.sendRedirect("/");
	}

}
