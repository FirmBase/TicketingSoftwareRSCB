package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class TicketBillSummaryDTO {
    public Long count;
    public String ticketName;
    public String groupName;
    public Double total;
}
