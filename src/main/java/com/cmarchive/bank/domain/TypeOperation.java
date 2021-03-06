package com.cmarchive.bank.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class TypeOperation {

	@Id
	@GeneratedValue
	private long id;
	
	private String valueType;

	public TypeOperation() {
		super();
	}

	public String getValue() {
		return valueType;
	}

	public TypeOperation setValue(String value) {
		this.valueType = value;
		return this;
	}
	
}
