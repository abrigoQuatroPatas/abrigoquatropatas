package br.com.compasso.client.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(max=10)
    private String number;
    private String district;

}
