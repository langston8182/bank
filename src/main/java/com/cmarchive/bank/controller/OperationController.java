package com.cmarchive.bank.controller;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String ajouter(Operation operation, Model model, HttpServletRequest request) {
		operation.setUser(loggedUser.getUser());
		int jourOperation = request.getParameter("jourOperation") != null ? Integer.parseInt(request.getParameter("jourOperation")) : 1;
		
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		if (request.getSession().getAttribute("month") != null) {
			month = (Integer) request.getSession().getAttribute("month");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.DAY_OF_MONTH, jourOperation);
		
		operationService.save(operation);

		return "redirect:/operations/list";
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
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, (int) request.getSession().getAttribute("month") - 1);
		operation.setDateOperation(calendar.getTime());
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
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
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

		// report d'operation du mois precedent
		float totalReport = total(operationService.findByMonth(this.loggedUser.getUser().getId(), month - 1));
		Operation operationReport = new Operation();
		operationReport.setPrix(totalReport);
		model.addAttribute("operationReport", operationReport);
		
		// liste des operations pernanentes a exclure
		Iterator<PermanentOperation> iterator = permanentsOperation.iterator();

		while (iterator.hasNext()) {
			PermanentOperation permanentOperation = iterator.next();
			for (Operation operation : operations) {
				if (operation.getPermanentOperation() != null
						&& (operation.getPermanentOperation().getId() == permanentOperation.getId())) {
					iterator.remove();
					break;
				}
			}
		}
	}

	private float total(List<Operation> operations) {
		float total = 0;
		for (Operation operation : operations) {
			if (operation.getTypeOperation().getValue().equals("DEBIT")) {
				total -= operation.getPrix();
			} else {
				total += operation.getPrix();
			}
		}

		return total;
	}
}
