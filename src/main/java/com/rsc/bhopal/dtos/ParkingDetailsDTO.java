package com.rsc.bhopal.dtos;

import java.util.Date;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.rsc.bhopal.entity.TicketsRatesMaster;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
public class ParkingDetailsDTO {
	
	private Long id;
	
	private String name;
	
	private String idDsec;
	
	private Date addedAt;
	
	private Boolean isActive;
  
   private TicketsRatesMasterDTO ticketsRatesMasterDTO;
}
