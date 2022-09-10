package br.com.compasso.voluntary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestPutAddressDto {

    @NotBlank(message = "Zip code cannot be null or empty")
    private String zipCode;
    @NotBlank
    private String street;
    @NotBlank
    private String city;
    @NotBlank
    private String state;
    @Size(max = 10)
    @NotBlank(message = "Number cannot be null or empty")
    private String number;
    @NotBlank
    private String district;

}
