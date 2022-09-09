package br.com.compasso.voluntary.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressEntity {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String district;
}
