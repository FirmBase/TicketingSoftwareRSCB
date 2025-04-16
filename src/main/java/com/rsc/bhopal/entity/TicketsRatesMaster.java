package com.rsc.bhopal.entity;

import java.util.Date;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rsc.bhopal.enums.BillType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "RSC_TS_TICKET_RATE_MASTER")
public class TicketsRatesMaster {	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TICKET_ID",referencedColumnName = "ID")
	private TicketDetails  ticketType;
	
	@ManyToOne
	@JoinColumn(name = "VISITOR_ID",referencedColumnName = "ID")
	private VisitorsType  visitorsType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARKING_DET_ID",referencedColumnName = "ID")
	private ParkingDetails  parkingDetails;
		
	@Column(name = "PRICE")
	private Float price;
	
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@Column(name = "REVISION_NO")
	private Integer revisionNo;
	
	@Column(name = "REVISED_AT")
	private Date revisedAt;
	
	@OneToOne(fetch =FetchType.LAZY)
	@JoinColumn(name = "REVISED_FROM",referencedColumnName = "ID")
    private TicketsRatesMaster oldRateMaster;
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "BILL_TYPE")
	private BillType billType;
	
	@ManyToOne
	@JoinColumn(name = "REVISED_BY",referencedColumnName = "ID")
    private RSCUser user;

	@Override
	public String toString(){
		return "NOT IMPLEMENTED";
		// return String.format("TicketsRatesMaster{ %d }", Math.round(price));
	}
}
