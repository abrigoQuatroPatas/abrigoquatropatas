package br.com.compasso.client.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddressDto {

    @NotBlank
    private String zipCode;
    private String street;
    private String city;
    private String state;
    @NotBlank
    private String number;
    private String district;

}
