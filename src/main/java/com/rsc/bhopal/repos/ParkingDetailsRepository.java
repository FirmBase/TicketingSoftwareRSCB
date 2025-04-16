package com.rsc.bhopal.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.ParkingDetails;

@Repository
public interface ParkingDetailsRepository extends JpaRepository<ParkingDetails, Long> {

	List<ParkingDetails> findByIsActive(boolean isActive);
	
}
