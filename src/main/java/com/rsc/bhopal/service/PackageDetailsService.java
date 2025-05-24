package com.rsc.bhopal.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rsc.bhopal.dtos.PackageDetailsDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.entity.PackageDetails;
import com.rsc.bhopal.entity.VisitorsType;
import com.rsc.bhopal.repos.PackageDetailsRepository;

@Service
public class PackageDetailsService {
	@Autowired
	private PackageDetailsRepository packageDetailsRepository;

	@Autowired
	private RSCUserDetailsService rscUserDetailsService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	public List<PackageDetailsDTO> getAllPackageDetails() {
		List<PackageDetailsDTO> packageDetailsDTOs = new ArrayList<PackageDetailsDTO>();
		packageDetailsRepository.findAll().forEach(packageDetail -> {
			PackageDetailsDTO packageDetailsDTO = new PackageDetailsDTO();
			BeanUtils.copyProperties(packageDetail, packageDetailsDTO);
			packageDetailsDTO.setAddedBy(packageDetail.getAddedBy().getUsername());
			// packageDetailsDTO.setBillDate(packageDetail.getBillDate());
			// packageDetailsDTO.setAddedBy(packageDetail.getAddedBy());

			List<VisitorsTypeDTO> visitorsTypeDTOs = new ArrayList<VisitorsTypeDTO>();
			packageDetail.getVisitorsTypes().forEach(visitorsType -> {
				VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
				BeanUtils.copyProperties(visitorsType, visitorsTypeDTO);
				visitorsTypeDTOs.add(visitorsTypeDTO);
			});
			packageDetailsDTO.setVisitorsType(visitorsTypeDTOs);

			packageDetailsDTOs.add(packageDetailsDTO);
		});
		return packageDetailsDTOs;
	}

	@Transactional
	public void addPackageDetails(PackageDetailsDTO packageDetailsDTO, String user) {
		PackageDetails packageDetail = new PackageDetails();
		BeanUtils.copyProperties(packageDetailsDTO, packageDetail);
		// packageDetail.setBillDate(packageDetailsDTO.getBillDate());
		// packageDetail.setAddedBy(packageDetailsDTO.getAddedBy());
		// packageDetail.setBillDate(new Date());
		packageDetail.setIsActive(true);
		packageDetail.setAddedBy(rscUserDetailsService.getUserByUsername(user));

		/*
		List<VisitorsTypeDTO> visitorsTypeDTOs = packageDetailsDTO.getVisitorsType();
		visitorsTypeDTOs.forEach(visitorsTypeDTO -> {
			if (visitorsTypeDTO.getId() != null) {
			}
		});
		*/
		Set<VisitorsType> visitorsTypes = new HashSet<VisitorsType>();
		/*
		for (int index = 0; index < visitorsTypeDTOs.size(); ++index) {
			final Long id = visitorsTypeDTOs.get(index).getId();
			if (id == null) {
				// visitorsTypeDTOs.remove(index);
			}
			else {
				VisitorsType visitorsType = visitorTypeService.getVisitorById(id)
					.orElseThrow(() -> new RuntimeException("No visitor found on this id"));
				visitorsTypes.add(visitorsType);
			}
		}
		*/
		packageDetailsDTO.getVisitorsType().forEach(visitorsTypeDTO -> {
			if (visitorsTypeDTO.getId() != null) {
				VisitorsType visitorsType = visitorTypeService.getVisitorById(visitorsTypeDTO.getId())
					.orElseThrow(() -> new RuntimeException("No visitor found on this id"));
				visitorsTypes.add(visitorsType);
			}
		});
		packageDetail.setVisitorsTypes(visitorsTypes);
		packageDetailsRepository.save(packageDetail);
	}

	public void togglePackage(Long id) {
		PackageDetails packageDetails = packageDetailsRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("Package not found"));
		packageDetails.setIsActive(!packageDetails.getIsActive());
		packageDetailsRepository.save(packageDetails);
	}
}
