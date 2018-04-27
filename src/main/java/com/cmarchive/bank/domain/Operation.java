package com.cmarchive.bank.domain;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
public class Operation {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@NotEmpty
	private String intitule;

	@Column
	private LocalDate dateOperation;
	
	@NotNull
	private float prix;
	
	@ManyToOne
	private TypeOperation typeOperation;
	
	@ManyToOne
	private User user;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "permanent_operation_id")
	private PermanentOperation permanentOperation;

	public Operation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public LocalDate getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(LocalDate dateOperation) {
		this.dateOperation = dateOperation;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PermanentOperation getPermanentOperation() {
		return permanentOperation;
	}

	public void setPermanentOperation(PermanentOperation permanentOperation) {
		this.permanentOperation = permanentOperation;
	}
	
}
