package br.com.compasso.client.entitys;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Address {

    @NotBlank
    private String zipCode;
    private String street;
    private String city;
    private String state;
    @NotBlank
    private String number;
    private String district;
}
