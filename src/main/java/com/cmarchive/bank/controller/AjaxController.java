package com.cmarchive.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.service.UserService;

@RestController
@RequestMapping("/ajax")
@Secured({"ROLE_ADMIN"})
public class AjaxController {

	private UserService userService;

	@Autowired
	public AjaxController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@ResponseBody
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public User get(@PathVariable(value = "id") Long id, Model model) {
		User user = userService.get(id);
		return user;
	}

}
