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

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = {"id", "intitule", "dateOperation", "prix"})
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
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "permanent_operation_id")
	private PermanentOperation permanentOperation;

	public Operation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public Operation setId(Long id) {
		this.id = id;
		return this;
	}

	public String getIntitule() {
		return intitule;
	}

	public Operation setIntitule(String intitule) {
		this.intitule = intitule;
        return this;
	}

	public LocalDate getDateOperation() {
		return dateOperation;
	}

	public Operation setDateOperation(LocalDate dateOperation) {
		this.dateOperation = dateOperation;
        return this;
	}

	public float getPrix() {
		return prix;
	}

	public Operation setPrix(float prix) {
		this.prix = prix;
        return this;
	}

	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	public Operation setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
        return this;
	}

	public User getUser() {
		return user;
	}

	public Operation setUser(User user) {
		this.user = user;
        return this;
	}

	public PermanentOperation getPermanentOperation() {
		return permanentOperation;
	}

	public Operation setPermanentOperation(PermanentOperation permanentOperation) {
		this.permanentOperation = permanentOperation;
        return this;
	}
}
