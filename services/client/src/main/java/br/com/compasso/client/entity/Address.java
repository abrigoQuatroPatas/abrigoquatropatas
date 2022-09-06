package br.com.compasso.client.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
