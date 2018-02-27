package com.cmarchive.bank.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmarchive.bank.component.LoggedUser;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.service.PermanentOperationService;
import com.cmarchive.bank.service.TypeOperationService;

@Controller
@RequestMapping("/permanentsOperation")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class PermanentOperationController {

	private PermanentOperationService permanentOperationService;
	private TypeOperationService typeOperationService;
	private LoggedUser loggedUser;

	@Autowired
	public PermanentOperationController(PermanentOperationService permanentOperationService, TypeOperationService typeOperationService, LoggedUser loggedUser) {
		super();
		this.permanentOperationService = permanentOperationService;
		this.typeOperationService = typeOperationService;
		this.loggedUser = loggedUser;
	}
	
	@RequestMapping("/list")
	public String list(Model model, HttpServletRequest request) {
		model.addAttribute("permanentsOperation", permanentOperationService.findByUser(loggedUser.getUser().getId()));
		model.addAttribute("typesOperation", typeOperationService.list());
		model.addAttribute("permanentOperation", new PermanentOperation());
		return "permanentsOperation/list";
	}
	
	@RequestMapping(value = "/ajouter", method = RequestMethod.POST)
	public String ajouter(PermanentOperation permanentOperation, Model model) {
		permanentOperationService.save(permanentOperation);
		
		return "redirect:/permanentsOperation/list";
	}
	
	@RequestMapping("/modifier/{id}")
	public String modifier(@PathVariable(value = "id") Long id, Model model) {
		PermanentOperation permanentOperation = permanentOperationService.get(id);
		model.addAttribute("permanentsOperation", permanentOperationService.list());
		model.addAttribute("typesOperation", typeOperationService.list());
		model.addAttribute("permanentOperation", permanentOperation);
		
		return "permanentsOperation/list";
	}
	
	@RequestMapping("/supprimer/{id}")
	public String supprimer(@PathVariable("id") Long id, Model model) {
		permanentOperationService.delete(id);
		
		return "redirect:/permanentsOperation/list";
	}
	
}
