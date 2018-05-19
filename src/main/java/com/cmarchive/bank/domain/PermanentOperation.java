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

@Data
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

}
