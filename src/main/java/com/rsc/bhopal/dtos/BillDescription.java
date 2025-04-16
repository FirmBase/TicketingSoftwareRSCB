package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class BillDescription {

   private String ticket;
   private String groupName;
   private int person;
   private double perPersonPrice;
   private double totalSum;
       
}
