package com.cmarchive.bank.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Operation {

	@Id
	@GeneratedValue
	private Long id;
	
	private String intitule;
	
	@CreatedDate
	@Column(columnDefinition = "TIMESTAMP")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateOperation;
	
	private float prix;
	
	@ManyToOne
	private TypeOperation typeOperation;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
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

	public Date getDateOperation() {
		return dateOperation;
	}

	public void setDateOperation(Date dateOperation) {
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
