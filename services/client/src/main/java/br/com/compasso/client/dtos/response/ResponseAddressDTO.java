package br.com.compasso.client.dtos.response;

import lombok.Data;

@Data
public class ResponseAddressDTO {
    private String street;
    private String city;
    private String state;
    private String number;
    private String zipCode;
    private String district;
}
