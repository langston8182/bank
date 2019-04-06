package com.cmarchive.bank.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@EqualsAndHashCode(of = {"id"})
public class User implements UserDetails {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	@NotEmpty
	private String nom;
	
	private String prenom;
	
	private String email;

	@NotNull
    @NotEmpty
	private String password;
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<Operation> operations = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
	@LazyCollection(LazyCollectionOption.FALSE)
	@JsonIgnore
	private List<PermanentOperation> permanentsOperation = new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Role> roles = new ArrayList<>();

	public User() {
		super();
	}

	public Long getId() {
		return id;
	}

	public User setId(Long id) {
		this.id = id;
        return this;
	}

	public String getNom() {
		return nom;
	}

	public User setNom(String nom) {
		this.nom = nom;
        return this;
	}

	public String getPrenom() {
		return prenom;
	}

	public User setPrenom(String prenom) {
		this.prenom = prenom;
        return this;
	}

	public String getEmail() {
		return email;
	}

	public User setEmail(String email) {
		this.email = email;
        return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
        return this;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public User setOperations(List<Operation> operations) {
		this.operations = operations;
        return this;
	}

	public List<PermanentOperation> getPermanentsOperation() {
		return permanentsOperation;
	}

	public User setPermanentsOperation(List<PermanentOperation> permanentsOperation) {
		this.permanentsOperation = permanentsOperation;
        return this;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public User setRoles(List<Role> roles) {
		this.roles = roles;
        return this;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}
}
