package com.cmarchive.bank.controller;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String home(HttpServletRequest request) {
		Calendar calendar = Calendar.getInstance();
		request.getSession().setAttribute("month", calendar.get(Calendar.MONTH) + 1);
		return "index";
	}
	
}
