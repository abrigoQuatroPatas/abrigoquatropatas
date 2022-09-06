package br.com.compasso.client.dto.response;

import lombok.Data;

@Data
public class ResponseAddressDto {
    private String street;
    private String city;
    private String state;
    private String number;
    private String zipCode;
    private String district;
}
