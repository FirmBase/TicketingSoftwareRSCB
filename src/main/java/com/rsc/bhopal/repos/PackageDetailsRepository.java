package com.rsc.bhopal.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.PackageDetails;

@Repository
public interface PackageDetailsRepository extends JpaRepository<PackageDetails, Long> {
}
