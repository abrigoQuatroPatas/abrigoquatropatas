package br.com.compasso.client.dtos.request;

import lombok.Data;

@Data
public class RequestAddressDTO {

    private String street;
    private String city;
    private String state;
    private String number;
    private String zipCode;
    private String district;

}
