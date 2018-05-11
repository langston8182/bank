package com.cmarchive.bank.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
public class Role {

	public Role() {
		super();
	}

	@Id
	@GeneratedValue
	public Long id;
	
	private String role;
}
