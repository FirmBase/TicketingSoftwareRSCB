package com.rsc.bhopal.dtos;

import java.util.Date;

import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.enums.VisitorsCategoryEnum;

import lombok.Data;

@Data
public class VisitorsTypeDTO {

	private Long id; 
	
	private String name; 
	
	private VisitorsCategoryEnum category; 

	private GroupType  groupType; 
	
	private Integer minMembers; 
	
    private Integer fixedMembers;
   
    private Date addedAt;
    
    private String addedBy;
    
    private Boolean isActive;
    
    private Boolean isDefault;
	
}
