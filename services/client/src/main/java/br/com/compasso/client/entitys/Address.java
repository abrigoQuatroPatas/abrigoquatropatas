package br.com.compasso.client.entitys;

import lombok.Data;

@Data
public class Address {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String district;
}
