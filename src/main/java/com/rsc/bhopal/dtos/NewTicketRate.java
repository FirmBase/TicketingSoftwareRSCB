package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class NewTicketRate {
    public long ticketId;
    public long groupId;
    public float price;
}
