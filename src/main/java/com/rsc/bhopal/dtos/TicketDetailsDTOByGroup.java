package com.rsc.bhopal.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDetailsDTOByGroup {
    
    private Long id;

    private String name;

    private Long groupId;
    
    private Float price;

    private Boolean checked; 

    TicketDetailsDTOByGroup(Long id,String name,Float price){
        this.id=id;
        this.name=name;
        this.price=price;
        this.checked=price==null?false:true;
    }

}
