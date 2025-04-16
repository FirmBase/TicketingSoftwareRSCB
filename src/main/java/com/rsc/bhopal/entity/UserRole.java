package com.rsc.bhopal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "RSC_TS_ROLES")
public class UserRole {
	  
		@Id
		@Column(name = "ID")
		@GeneratedValue(strategy = GenerationType.AUTO)
		private Long id;
				
		@Column(name = "ROLE")
		private String role;
	
}
