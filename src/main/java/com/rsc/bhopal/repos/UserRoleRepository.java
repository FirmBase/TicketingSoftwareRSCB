package com.rsc.bhopal.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.UserRole;
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
