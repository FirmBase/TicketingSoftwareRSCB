package com.rsc.bhopal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.dtos.RSCUserDTO;
import com.rsc.bhopal.dtos.UserRoleDTO;
import com.rsc.bhopal.entity.RSCUser;
import com.rsc.bhopal.entity.UserRole;
import com.rsc.bhopal.repos.UserDetailsRepository;
import com.rsc.bhopal.repos.UserRoleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RSCUserDetailsService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserDetailsRepository userRepo;

	@Autowired
	private UserRoleRepository roleRepo;

	public RSCUser getUserByUsername(String username) {
		return userRepo.findByUsername(username).get();
	}

	public List<UserRoleDTO> getAllRoles(){
		List<UserRoleDTO> roleDTOs = new ArrayList<>();
		List<UserRole> roles = roleRepo.findAll();
		roles.forEach(role -> {
			UserRoleDTO dto = new UserRoleDTO();
			BeanUtils.copyProperties(role,dto);
			roleDTOs.add(dto);
		});
		return roleDTOs;
	}
	
	public void addUser(RSCUserDTO dto) {
		RSCUser user = new RSCUser();
		BeanUtils.copyProperties(dto, user);
		user.setPassword(encoder.encode(dto.getPassword()));
		user.setAddedAt(new Date());
		user.setIsActive(true);
		user.setRootUser(false);
		List<UserRole> roles = roleRepo.findAllById(dto.getRoles());
		log.debug(user.toString());
		user.setRoles(roles.stream().collect(Collectors.toSet()));
		user = userRepo.save(user);
	}

	public List<RSCUserDTO> getAllUser(){
		 List<RSCUser> users = userRepo.findAll();
		 List<RSCUserDTO> dtos = new ArrayList<RSCUserDTO>();
		 users.forEach(user -> {
			RSCUserDTO dto = new RSCUserDTO();
			BeanUtils.copyProperties(user, dto);
			Set<UserRoleDTO> rolesDTOs = new HashSet<>();
			if(user.getRoles()!=null) {
			   user.getRoles().forEach(role -> {
					UserRoleDTO roleDTO = new UserRoleDTO();
					BeanUtils.copyProperties(role, roleDTO);
					rolesDTOs.add(roleDTO);
			   });
			   dto.setRolesDTO(rolesDTOs);
			 }
			 dtos.add(dto);
		 });
		return dtos;
	}

	public void changeUserPassword(String username, String password) {
		Optional<RSCUser> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
			user.get().setPassword(encoder.encode(password));
			userRepo.save(user.get());
		}
	}

	public void changeUserStatus(String username) {
		Optional<RSCUser> user = userRepo.findByUsername(username);
		if(user.isPresent()) {
			if(!user.get().getRootUser()) {
				user.get().setIsActive(!user.get().getIsActive());
				userRepo.save(user.get());
			}
		}
	}
}
