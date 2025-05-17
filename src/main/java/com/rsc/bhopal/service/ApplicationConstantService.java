package com.rsc.bhopal.service;

import java.util.List;
import java.util.Optional;
import java.math.BigInteger;
import java.util.ArrayList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.dtos.ApplicationConstantDTO;
import com.rsc.bhopal.dtos.PrintAdjustDTO;
import com.rsc.bhopal.entity.ApplicationConstant;
import com.rsc.bhopal.enums.ApplicationConstantType;
import com.rsc.bhopal.repos.ApplicationConstantRepository;
import com.rsc.bhopal.utills.CommonUtills;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApplicationConstantService {
	@Autowired
	private ApplicationConstantRepository applicationConstantRepository;

	public List<ApplicationConstantDTO> getAllApplicationConstants() {
		List<ApplicationConstantDTO> applicationConstantDTOs = new ArrayList<ApplicationConstantDTO>();
		applicationConstantRepository.findAll().forEach(applicationConstant -> {
			ApplicationConstantDTO applicationConstantDTO = new ApplicationConstantDTO();
			BeanUtils.copyProperties(applicationConstant, applicationConstantDTO);
			applicationConstantDTOs.add(applicationConstantDTO);
		});
		return applicationConstantDTOs;
	}

	public List<ApplicationConstantDTO> getAllTicketsSerial() {
		List<ApplicationConstantDTO> applicationConstantDTOs = new ArrayList<ApplicationConstantDTO>();
		applicationConstantRepository.getAllTicketsSerial().forEach(applicationConstant -> {
			ApplicationConstantDTO applicationConstantDTO = new ApplicationConstantDTO();
			BeanUtils.copyProperties(applicationConstant, applicationConstantDTO);
			applicationConstantDTOs.add(applicationConstantDTO);
		});
		return applicationConstantDTOs;
	}

	public ApplicationConstantDTO getTicketSerial() {
		ApplicationConstantDTO applicationConstantDTO = new ApplicationConstantDTO();
		applicationConstantRepository.getAllTicketsSerial().forEach(applicationConstant -> {
			BeanUtils.copyProperties(applicationConstant, applicationConstantDTO);
		});
		return applicationConstantDTO;
	}

	public void replaceTicketSerial(long id, String ticketSerial) {
		Optional<ApplicationConstant> applicationConstantOptional = applicationConstantRepository.findById(id);
		if (applicationConstantOptional.isPresent()) {
			ApplicationConstant applicationConstant = applicationConstantOptional.get();
			applicationConstant.setData(ticketSerial);
			applicationConstantRepository.save(applicationConstant);
		}
	}

	public ApplicationConstantDTO getBillSeries() {
		ApplicationConstantDTO applicationConstantDTO = new ApplicationConstantDTO();
		applicationConstantRepository.getAllBillSeries().forEach(applicationConstant -> {
			BeanUtils.copyProperties(applicationConstant, applicationConstantDTO);
		});
		return applicationConstantDTO;
	}

	public void replaceBillSeries(long id, String billSeries) {
		Optional<ApplicationConstant> applicationConstantOptional = applicationConstantRepository.findById(id);
		if (applicationConstantOptional.isPresent()) {
			ApplicationConstant applicationConstant = applicationConstantOptional.get();
			applicationConstant.setData(billSeries);
			applicationConstantRepository.save(applicationConstant);
		}
	}

	public ApplicationConstantDTO getBillSerialStart() {
		ApplicationConstantDTO applicationConstantDTO = new ApplicationConstantDTO();
		applicationConstantRepository.getAllBillSerialStart().forEach(applicationConstant -> {
			BeanUtils.copyProperties(applicationConstant, applicationConstantDTO);
		});
		return applicationConstantDTO;
	}

	public void replaceBillSerialStart(long id, String billSerialStart) {
		Optional<ApplicationConstant> applicationConstantOptional = applicationConstantRepository.findById(id);
		if (applicationConstantOptional.isPresent()) {
			ApplicationConstant applicationConstant = applicationConstantOptional.get();
			applicationConstant.setData(billSerialStart);
			applicationConstantRepository.save(applicationConstant);
		}
	}

	public ApplicationConstantDTO getBillSerialEnd() {
		ApplicationConstantDTO applicationConstantDTO = new ApplicationConstantDTO();
		applicationConstantRepository.getAllBillSerialEnd().forEach(applicationConstant -> {
			BeanUtils.copyProperties(applicationConstant, applicationConstantDTO);
		});
		return applicationConstantDTO;
	}

	public void replaceBillSerialEnd(long id, String billSerialEnd) {
		Optional<ApplicationConstant> applicationConstantOptional = applicationConstantRepository.findById(id);
		if (applicationConstantOptional.isPresent()) {
			ApplicationConstant applicationConstant = applicationConstantOptional.get();
			applicationConstant.setData(billSerialEnd);
			applicationConstantRepository.save(applicationConstant);
		}
	}

	public ApplicationConstantDTO getBillSerial() {
		ApplicationConstantDTO applicationConstantDTO = new ApplicationConstantDTO();
		applicationConstantRepository.getAllBillSerial().forEach(applicationConstant -> {
			BeanUtils.copyProperties(applicationConstant, applicationConstantDTO);
		});
		return applicationConstantDTO;
	}

	public void replaceBillSerial(long id, String billSerial) {
		Optional<ApplicationConstant> applicationConstantOptional = applicationConstantRepository.findById(id);
		if (applicationConstantOptional.isPresent()) {
			ApplicationConstant applicationConstant = applicationConstantOptional.get();
			applicationConstant.setData(billSerial);
			applicationConstantRepository.save(applicationConstant);
		}
	}

	public List<PrintAdjustDTO> getAllCurrentPrintCoordinate() {
		List<PrintAdjustDTO> printAdjustDTOs = new ArrayList<PrintAdjustDTO>();
		applicationConstantRepository.getAllCurrentPrintCoordinates().forEach(applicationConstant -> {
			PrintAdjustDTO printAdjustDTO = CommonUtills.convertJSONToObject(applicationConstant.getData(), PrintAdjustDTO.class);
			printAdjustDTO.setId(applicationConstant.getId());
			printAdjustDTOs.add(printAdjustDTO);
		});
		return printAdjustDTOs;
	}

	public void setAllCurrentPrintCoordinates(List<PrintAdjustDTO> printAdjustDTOs) {
		printAdjustDTOs.forEach(printAdjustDTO -> {
			Optional<ApplicationConstant> applicationConstant = applicationConstantRepository.findById(printAdjustDTO.getId());
			try {
				if (applicationConstant.isPresent()) {
					applicationConstant.get().setData(CommonUtills.convertToJSON(printAdjustDTO));
					applicationConstant.get().setType(ApplicationConstantType.TICKET_PRINT_COORDINATE);
					applicationConstantRepository.save(applicationConstant.get());
				}
				else {
					ApplicationConstant newApplicationConstant = new ApplicationConstant();
					newApplicationConstant.setId(null);
					newApplicationConstant.setData(CommonUtills.convertToJSON(printAdjustDTO));
					newApplicationConstant.setType(ApplicationConstantType.TICKET_PRINT_COORDINATE);
					applicationConstantRepository.save(newApplicationConstant);
				}
			}
			catch(JsonProcessingException ex) {
				log.debug("Exception converting " + printAdjustDTO + " to JSON: " + ex.getMessage());
			}
		});
	}

	public boolean checkBillSerialInRange() {
		boolean status = false;
		try {
			status = (new BigInteger(getBillSerialStart().getData()).compareTo(new BigInteger(getBillSerial().getData())) != 1)
				&&
				(new BigInteger(getBillSerialEnd().getData()).compareTo(new BigInteger(getBillSerial().getData())) != -1);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
		return status;
	}
}
