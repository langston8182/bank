package com.cmarchive.bank.controller;

import java.security.Principal;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.cmarchive.bank.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String home(HttpServletRequest request) {
		Calendar calendar = Calendar.getInstance();
		request.getSession().setAttribute("month", calendar.get(Calendar.MONTH) + 1);
		return "index";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("securedPage")
	public String securedPage(Model model, OAuth2Authentication auth, Principal principal) {
        return "securedPage";
	}
}
