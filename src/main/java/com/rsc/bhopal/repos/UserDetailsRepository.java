package com.rsc.bhopal.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.RSCUser;

@Repository
public interface UserDetailsRepository extends JpaRepository<RSCUser, Long> {

	Optional<RSCUser> findByUsername(String username);
	
	Optional<RSCUser> findByUsernameAndIsActive(String username,boolean isActive);
}
