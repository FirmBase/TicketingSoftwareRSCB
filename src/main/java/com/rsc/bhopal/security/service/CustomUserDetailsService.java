package com.rsc.bhopal.security.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.entity.RSCUser;
import com.rsc.bhopal.repos.UserDetailsRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private UserDetailsRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<RSCUser> user = userRepo.findByUsernameAndIsActive(username,true);
		UserDetails userDetails = null;
		if (user.isPresent()) {

			List<String> roles = user.get().getRoles().stream().map(role -> role.getRole().toString().toUpperCase())
					.collect(Collectors.toList());
			
			
			userDetails = User.withUsername(user.get()
					                            .getUsername())
					                            .password(user.get().getPassword())
					                            .roles(roles.toArray(new String[roles.size()]))
					                            .build();

		} else {
			throw new UsernameNotFoundException("User is not Present in the DB");
		}
		return userDetails;

	}

}
