package com.rsc.bhopal.repos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.ActivityLog;

@Repository
public interface ActivityLogRepo extends CrudRepository<ActivityLog, Long>,PagingAndSortingRepository<ActivityLog, Long> {	
  
	@Query(value="from ActivityLog")
	List<ActivityLog> recentLogs(Pageable pageable);
	
}

