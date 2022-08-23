package br.com.compasso.ONG.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RequestAddressDto {

    @NotBlank
    private String zipCode;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    private String number;
    @NotBlank
    private String district;

}
