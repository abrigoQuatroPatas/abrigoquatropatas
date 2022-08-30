package br.com.compasso.voluntary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAddressDto {

    @NotEmpty(message = "Zip code cannot be null or empty")
    private String zipCode;
    @NotEmpty(message = "Street cannot be null or empty")
    private String street;
    @NotEmpty(message = "City cannot be null or empty")
    private String city;
    @NotEmpty(message = "State cannot be null or empty")
    private String state;
    @NotEmpty(message = "Number cannot be null or empty")
    private String number;
    @NotEmpty(message = "District cannot be null or empty")
    private String district;
}
