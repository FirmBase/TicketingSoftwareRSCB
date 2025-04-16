package com.rsc.bhopal.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ParkingPriceDTOWrapper {
    List<ParkingPriceDTO> parkingRates;
}
