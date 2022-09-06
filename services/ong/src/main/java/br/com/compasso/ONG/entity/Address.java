package br.com.compasso.ONG.entity;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String district;
}
