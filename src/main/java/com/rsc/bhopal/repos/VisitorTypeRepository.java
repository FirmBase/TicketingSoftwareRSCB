package com.rsc.bhopal.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.VisitorsType;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.enums.VisitorsCategoryEnum;

@Repository
public interface VisitorTypeRepository extends JpaRepository<VisitorsType, Long> {

	List<VisitorsType> findByCategory(VisitorsCategoryEnum category);

	List<VisitorsType> findByIsActive(Boolean isActive);
	
	List<VisitorsType>  findByGroupTypeAndIsActive(GroupType groupType,Boolean isActive);
	
	//List<ComboVisitorRateTicketsDTO> getComboVisitorRateTickets(long id);
	
	@Modifying
	@Query(value="update VisitorsType set isDefault = false where isDefault = true")
	void removeDefaults();
	
}
