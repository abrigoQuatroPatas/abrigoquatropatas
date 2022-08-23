package br.com.compasso.ONG.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
public class Address {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String district;
}