package br.com.compasso.voluntary.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ResponseAddressDto {

    private String zipCode;
    private String street;
    private String city;
    private String state;
    private String number;
    private String district;

}
