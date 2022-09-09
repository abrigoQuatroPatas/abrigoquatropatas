package br.com.compasso.pet.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RedemptionAddressResponseDto {

    private String state;

    private String city;

    private String district;

    private String zipCode;

    private String street;

    private String number;

    private LocalDateTime redemptionDate;
}
