package com.rsc.bhopal.entity;

import java.util.Date;
import java.util.List;
import java.math.BigInteger;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "RSC_TS_TICKET_BILL")
public class TicketBill {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "GENERATED_AT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date generatedAt;

	@Column(name = "TOTAL_BILL")
	private Double totalBill;

	@Column(name = "PERSONS")
	private int persons;

	@Column(name = "TICKET_PAYLOAD", length = 3000)
	private String ticketPayload;

	@Column(name = "INTITUTION")
	private String institution;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "TICKET_SERIAL")
	private BigInteger ticketSerial;

	@Column(name = "CANCELLED_STATUS")
	private Boolean cancelledStatus;

	@ManyToOne
	@JoinColumn(name = "GENERATED_BY", referencedColumnName = "ID")
	private RSCUser generatedBy;

	@JsonIgnore
	@OneToMany(mappedBy = "generatedTicket", fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	List<TicketBillRow> billSummary;

/*
	@ManyToOne(fetch = FetchType.EARLY)
	@JoinColumn(name = "SERIAL_ID", referencedColumnName = "ID")
	private ApplicationConstant applicationConstant;
*/

	@Override
	public String toString() {
		return "GeneratedTicket [id=" + id + ", generatedAt=" + generatedAt + ", totalBill=" + totalBill + ", generatedBy="
				+ generatedBy + "]";
	}
}
