package com.rsc.bhopal.dtos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class RSCUserDTO {
	
	private Long id;
	
	private String name;
	
	private String username;
	
	private String password;
	
	private Boolean rootUser;
	
	private Date addedAt;
	
	private Boolean isActive;
	
	private List<Long> roles;
	
	private Set<UserRoleDTO> rolesDTO;
}
