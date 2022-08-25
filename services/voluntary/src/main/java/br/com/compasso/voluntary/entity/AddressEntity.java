package br.com.compasso.voluntary.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class AddressEntity {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String district;
}
