package br.com.compasso.voluntary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestAddressDto {

    @NotEmpty(message = "Zip code cannot be null or empty")
    private String zipCode;
    private String street;
    private String city;
    private String state;
    @Size(max = 10)
    @NotEmpty(message = "Number cannot be null or empty")
    private String number;
    private String district;

}
