package br.com.compasso.ONG.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class RequestPutAddressDto {

    @NotBlank
    private String zipCode;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @NotBlank
    @Size(max = 10)
    private String number;
    @NotBlank
    private String district;

}
