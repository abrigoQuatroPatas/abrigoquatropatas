package br.com.compasso.client.entities;

import lombok.Data;

@Data
public class Address {

    private String street;
    private String city;
    private String state;
    private String number;
    private String zipCode;
    private String district;
}
