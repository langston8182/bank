package com.cmarchive.bank.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cmarchive.bank.component.LoggedUser;
import com.cmarchive.bank.domain.Operation;
import com.cmarchive.bank.domain.PermanentOperation;
import com.cmarchive.bank.service.OperationService;
import com.cmarchive.bank.service.PermanentOperationService;
import com.cmarchive.bank.service.TypeOperationService;

@Controller
@RequestMapping("/operations")
@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@Scope("session")
public class OperationController {

	private LoggedUser loggedUser;
	private OperationService operationService;
	private TypeOperationService typeOperationService;
	private PermanentOperationService permanentOperationService;

	@Autowired
	public OperationController(OperationService operationService, TypeOperationService typeOperationService,
			PermanentOperationService permanentOperationService, LoggedUser loggedUser) {
		super();
		this.operationService = operationService;
		this.typeOperationService = typeOperationService;
		this.permanentOperationService = permanentOperationService;
		this.loggedUser = loggedUser;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, Model model) {
		fillModelForList(model, request);

		return "operations/list";
	}

	@RequestMapping(value = "/list/{month}", method = RequestMethod.GET)
	public String listByMonth(@PathVariable(value = "month") int month, Model model, HttpServletRequest request) {
		request.getSession().setAttribute("month", month);
		fillModelForList(model, request);

		return "operations/list";
	}

	@RequestMapping(value = "/ajouter", method = RequestMethod.POST)
	public String ajouter(@Valid Operation operation, BindingResult bindingResult, Model model, HttpServletRequest request) {
		if (bindingResult.hasErrors()) {
			fillModelForList(model, request);
			return "operations/list";
		} else {

			operation.setUser(loggedUser.getUser());
			int jourOperation = request.getParameter("jourOperation") != null ? Integer.parseInt(request.getParameter("jourOperation")) : 1;

			int month = LocalDate.now().getMonthValue();
			if (request.getSession().getAttribute("month") != null) {
				month = (Integer) request.getSession().getAttribute("month");
			}
			
			LocalDate localDate = LocalDate.of(Year.now().getValue(), month, jourOperation);

			operation.setDateOperation(localDate);

			operationService.save(operation);

			return "redirect:/operations/list";
		}
	}

	@RequestMapping(value = "/ajoutReport", method = RequestMethod.POST)
	public String ajouterReport(Operation operation, Model model, HttpServletRequest request) {
		operation.setUser(loggedUser.getUser());
		if (operation.getPrix() < 0) {
			operation.setTypeOperation(typeOperationService.getDebit());
		} else {
			operation.setTypeOperation(typeOperationService.getCredit());
		}
		operation.setPrix(Math.abs(operation.getPrix()));
		operation.setIntitule("Report");
		
		LocalDate localDate = LocalDate.of(Year.now().getValue(), (int) request.getSession().getAttribute("month"), 1);

		operation.setDateOperation(localDate);
		operationService.save(operation);
		
		return "redirect:/operations/list";
	}
	
	@RequestMapping(value = "/supprimer/{id}")
	public String supprimer(@PathVariable(value = "id") Long id) {
		operationService.delete(id);

		return "redirect:/operations/list";
	}

	@RequestMapping(value = "/modifier/{id}")
	public String modifier(@PathVariable(value = "id") Long id, Model model, HttpServletRequest request) {
		Operation operation = operationService.get(id);
		fillModelForList(model, request);
		model.addAttribute("operation", operation);

		return "operations/list";
	}

	@RequestMapping(value = "/ajouterOpPermanente/{idOpPermanente}")
	public String ajouterOpPermanente(@PathVariable(value = "idOpPermanente") Long idOpPermanente, Model model,
			HttpServletRequest request) {
		PermanentOperation permanentOperation = permanentOperationService.get(idOpPermanente);
		int month = (Integer) request.getSession().getAttribute("month");
		operationService.addOpPermanenteToOperations(month, permanentOperation);

		return "redirect:/operations/list";
	}

	private void fillModelForList(Model model, HttpServletRequest request) {
		int month = LocalDate.now().getMonthValue();
		if (request.getSession().getAttribute("month") != null) {
			month = (Integer) request.getSession().getAttribute("month");
		}
		List<Operation> operations = operationService.findByMonth(loggedUser.getUser().getId(), month);
		List<PermanentOperation> permanentsOperation = permanentOperationService
				.findByUser(loggedUser.getUser().getId());
		model.addAttribute("operations", operations);
		model.addAttribute("typesOperation", typeOperationService.list());
		model.addAttribute("operation", new Operation());
		model.addAttribute("total", total(operations));
		model.addAttribute("permanentsOperation", permanentsOperation);
		model.addAttribute("jourOperation", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

		// report d'operation du mois precedent
		float totalReport = total(operationService.findByMonth(this.loggedUser.getUser().getId(), month == 1 ? month : month - 1));
		Operation operationReport = new Operation();
		operationReport.setPrix(totalReport);
		model.addAttribute("operationReport", operationReport);
		
		// liste des operations pernanentes a exclure
		List<PermanentOperation> permanentOperationsToDelebe = permanentsOperation.stream()
		    .filter(p -> operations.stream()
		            .filter(o -> o.getPermanentOperation() != null)
		            .anyMatch(o -> o.getPermanentOperation().getId().equals(p.getId())))
		    .collect(Collectors.toList());
		
		permanentsOperation.removeAll(permanentOperationsToDelebe);
	}

	private float total(List<Operation> operations) {
		
	    float total = 0;
	    
		double totalCredit = operations.stream()
		        .filter(o -> o.getTypeOperation().getValue().equals("CREDIT"))
		        .mapToDouble(o -> o.getPrix())
		        .sum();
		
		double totalDebit = operations.stream()
                .filter(o -> o.getTypeOperation().getValue().equals("DEBIT"))
                .mapToDouble(o -> o.getPrix())
                .sum();
		
		total = (float) (totalCredit - totalDebit);
		return total;
	}
}
