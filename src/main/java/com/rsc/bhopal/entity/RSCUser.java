package com.rsc.bhopal.entity;

import java.util.Date;
import java.util.Set;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "RSC_TS_USERS")
public class RSCUser {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "USERNAME", unique = true)
	private String username;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "ADDED_AT")	
	private Date addedAt;

	@Column(name = "IS_ACTIVE")
	private Boolean isActive;

	@Column(name = "ROOT_USER")
	private Boolean rootUser;

	@ManyToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	@JoinTable(name = "RSC_TS_USER_ROLES", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
	private Set<UserRole> roles;
}
