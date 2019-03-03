package com.cmarchive.bank.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(of = {"id"})
public class PermanentOperation {

	public PermanentOperation() {
		super();
	}

	@Id
	@GeneratedValue
	private Long id;
	
	private String intitule;
	
	private int jour;
	
	private float prix;
	
	@ManyToOne
	private TypeOperation typeOperation;
	
	@ManyToOne
	private User user;

	@OneToMany(mappedBy = "permanentOperation")
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Operation> operations = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public PermanentOperation setId(Long id) {
		this.id = id;
		return this;
	}

	public String getIntitule() {
		return intitule;
	}

	public PermanentOperation setIntitule(String intitule) {
		this.intitule = intitule;
        return this;
	}

	public int getJour() {
		return jour;
	}

	public PermanentOperation setJour(int jour) {
		this.jour = jour;
        return this;
	}

	public float getPrix() {
		return prix;
	}

	public PermanentOperation setPrix(float prix) {
		this.prix = prix;
        return this;
	}

	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	public PermanentOperation setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
        return this;
	}

	public User getUser() {
		return user;
	}

	public PermanentOperation setUser(User user) {
		this.user = user;
        return this;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public PermanentOperation setOperations(List<Operation> operations) {
		this.operations = operations;
        return this;
	}
}
