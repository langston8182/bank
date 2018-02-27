package com.cmarchive.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmarchive.bank.domain.User;
import com.cmarchive.bank.service.UserService;

@Controller
@RequestMapping("/users")
@Scope("session")
@Secured({"ROLE_ADMIN"})
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("users", userService.list());
		model.addAttribute("user", new User());
		return "users/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable(value = "id") Long id, Model model) {
		model.addAttribute("user", userService.get(id));

		return "users/view";
	}

	@RequestMapping("/modifier")
	public String modifier(User user) {
		User userBefore = null;
		if (user.getId() != null) {
			userBefore = userService.get(user.getId());
			user.setPassword(userBefore.getPassword());
		} else {
			userService.encodePassword(user);
			userService.setUserRole(user);
		}
		userService.save(user);

		return "redirect:/users/list";
	}
	
	@RequestMapping("/supprimer/{id}")
	public String supprimer(@PathVariable(value = "id") Long id) {
		userService.delete(id);
		
		return "redirect:/users/list";
	}
}
