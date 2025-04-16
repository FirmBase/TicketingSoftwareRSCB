package com.rsc.bhopal.dtos;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class PackageDetailsDTO {
	private Long id;

	private String name;

	private String receiptNo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date billDate;

	private Float price;

	private Boolean isActive;

	private String addedBy;

	private List<VisitorsTypeDTO> visitorsType;
}
