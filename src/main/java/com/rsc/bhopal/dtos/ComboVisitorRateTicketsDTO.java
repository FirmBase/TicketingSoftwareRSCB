package com.rsc.bhopal.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ComboVisitorRateTicketsDTO {
    private VisitorsTypeDTO visitorsTypeDTO;
    private List<TicketDetailsDTO> ticketDetailsDTO;
}
