package com.cmarchive.bank.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(of = {"id"})
public class Role {

	public Role() {
		super();
	}

	@Id
	@GeneratedValue
	public Long id;
	
	private String role;

	public Long getId() {
		return id;
	}

	public Role setId(Long id) {
		this.id = id;
		return this;
	}

	public String getRole() {
		return role;
	}

	public Role setRole(String role) {
		this.role = role;
		return this;
	}
}
