package br.com.compasso.ONG.dto.response;

import lombok.Data;

@Data
public class ResponseAddressDto {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String district;
}
