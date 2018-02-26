package com.cmarchive.bank.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TypeOperation {

	@Id
	@GeneratedValue
	private long id;
	
	private String valueType;

	public TypeOperation() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return valueType;
	}

	public void setValue(String value) {
		this.valueType = value;
	}
	
}
