package com.rsc.bhopal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.annotations.RSCLog;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.entity.RSCUser;
import com.rsc.bhopal.entity.VisitorsType;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.enums.VisitorsCategoryEnum;
import com.rsc.bhopal.repos.VisitorTypeRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VisitorTypeService {	

	@Autowired
	private VisitorTypeRepository visitorTypeRepo;

	@Autowired
	private RSCUserDetailsService userDetailsService;

	public List<VisitorsTypeDTO> getComboVisitorTypes( ) {
		List<VisitorsTypeDTO> visitorsDTOs = new ArrayList<VisitorsTypeDTO>();
		List<VisitorsType> visitors = visitorTypeRepo.findByGroupTypeAndIsActive(GroupType.COMBO,true);
		for (VisitorsType visitor : visitors) {
			VisitorsTypeDTO visitorDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(visitor, visitorDTO);
			visitorsDTOs.add(visitorDTO);
		}
		return visitorsDTOs;
	}

	@Transactional(value = TxType.REQUIRED)
	public void changeVisitorStatus(Long visitorId) {
		Optional<VisitorsType> visitor =  visitorTypeRepo.findById(visitorId);
		
		if(visitor.get().getIsDefault()==false)
		    visitor.get().setIsActive(!visitor.get().getIsActive());
		
		visitorTypeRepo.save(visitor.get());
	}

	@Transactional(value = TxType.REQUIRED)
	public void changeDefaultVisitor(Long visitorId) {
		log.debug("Removing all defaults");
		visitorTypeRepo.removeDefaults();		
		Optional<VisitorsType> visitor =  visitorTypeRepo.findById(visitorId);
		visitor.get().setIsDefault(true);		
		visitorTypeRepo.save(visitor.get());
	}

	@RSCLog(desc="Visitors add request")
	public void addVisitorType(VisitorsTypeDTO dto,String username) {		
		VisitorsType visitorType = new VisitorsType();		
		BeanUtils.copyProperties(dto, visitorType);
		RSCUser user = userDetailsService.getUserByUsername(username);
		visitorType.setAddedAt(new Date());
		visitorType.setIsActive(true);
		visitorType.setIsDefault(false);
		visitorType.setAddedBy(user);
		visitorTypeRepo.save(visitorType);	
	}

	public Optional<VisitorsType> getVisitorById(long visitorid) {
		Optional<VisitorsType> visitorsType = visitorTypeRepo.findById(visitorid);		
		return visitorsType;
	}

	/*
	public VisitorsTypeDTO getVisitorsTypeDTOById(Long visitorid) {
		VisitorsType visitorsType = visitorTypeRepo.findById(visitorid)
			.orElseThrow(() -> new RuntimeException("No visitor found related to id"));;
		VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
		BeanUtils.copyProperties(visitorsType, visitorsTypeDTO);
		return visitorsTypeDTO;
	}
	*/

	public VisitorsTypeDTO getGeneralVisitorId(long visitorid) {
		VisitorsTypeDTO visitorDTO = new VisitorsTypeDTO();
		Optional<VisitorsType> visitorsType = visitorTypeRepo.findById(visitorid);
		BeanUtils.copyProperties(visitorsType.get(), visitorDTO);
		return visitorDTO;
	}

	public List<VisitorsTypeDTO> getFamilyVisitorTypes(VisitorsCategoryEnum typeEnum) {
		List<VisitorsTypeDTO> visitorsDTOs = new ArrayList<VisitorsTypeDTO>();
		List<VisitorsType> visitors = visitorTypeRepo.findByCategory(typeEnum);
		for (VisitorsType visitor : visitors) {
			VisitorsTypeDTO visitorDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(visitor, visitorDTO);
			visitorsDTOs.add(visitorDTO);
		}
		return visitorsDTOs;
	}

	public List<VisitorsTypeDTO> getAllActiveVisitorTypes() {
		List<VisitorsTypeDTO> visitorsDTOs = new ArrayList<VisitorsTypeDTO>();
		List<VisitorsType> visitors = visitorTypeRepo.findByIsActive(true);
		for (VisitorsType visitor : visitors) {
			VisitorsTypeDTO visitorDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(visitor, visitorDTO);
			visitorDTO.setAddedBy(visitor.getAddedBy().getName());
			visitorsDTOs.add(visitorDTO);
		}
		return visitorsDTOs;
	}

	public List<VisitorsTypeDTO> getVisitorTypeByGroupType(GroupType groupType) {
		List<VisitorsTypeDTO> visitorsDTOs = new ArrayList<VisitorsTypeDTO>();
		List<VisitorsType> visitors = visitorTypeRepo.findByGroupTypeAndIsActive(groupType,true);
		for (VisitorsType visitor : visitors) {
			VisitorsTypeDTO visitorDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(visitor, visitorDTO);
			visitorDTO.setAddedBy(visitor.getAddedBy().getName());
			visitorsDTOs.add(visitorDTO);
		}
		return visitorsDTOs;
	}

	public List<VisitorsTypeDTO> getAllVisitorTypes() {
		List<VisitorsTypeDTO> visitorsDTOs = new ArrayList<VisitorsTypeDTO>();
		List<VisitorsType> visitors = visitorTypeRepo.findAll();
		for (VisitorsType visitor : visitors) {
			VisitorsTypeDTO visitorDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(visitor, visitorDTO);
			visitorDTO.setAddedBy(visitor.getAddedBy().getName());
			visitorsDTOs.add(visitorDTO);
		}
		return visitorsDTOs;
	}

	@Transactional
	public void addVisitorType() {
		List<VisitorsType> types = Arrays.asList(new VisitorsType("General Visitors", VisitorsCategoryEnum.GENERAL, 4, 0),

				new VisitorsType("More than 25 People", VisitorsCategoryEnum.GROUP, 25, 0),
				new VisitorsType("School Group", VisitorsCategoryEnum.SCHOOL, 0, 0),
				new VisitorsType("Govt.or Municipal School", VisitorsCategoryEnum.SCHOOL, 0, 0),

				new VisitorsType("Family Group (Group of 4)", VisitorsCategoryEnum.FAMILY, 4, 0),
				new VisitorsType("Family Group (Group of 6)", VisitorsCategoryEnum.FAMILY, 6, 0),

				new VisitorsType("Sponsored Visitors", VisitorsCategoryEnum.SPONSORED, 0, 0),
				new VisitorsType("Free Entry", VisitorsCategoryEnum.OTHER, 0, 0),
				new VisitorsType("Special Case", VisitorsCategoryEnum.SPECIAL, 0, 0)

		);
		types.forEach(System.out::println);
		visitorTypeRepo.saveAll(types);
	}

}
