package com.rsc.bhopal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "RSC_TS_TICKET_BILL_ROWS")
public class TicketSummary {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "TOTAL_SUM")
	private Long totalSum;

	@Column(name = "BILL_ID")
	private Long billId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RATE_MASTER_ID", referencedColumnName = "ID")
	private TicketsRatesMaster ticketsRatesMaster;
}
