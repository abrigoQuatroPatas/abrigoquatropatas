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
public class RequestAddressDto {

    @NotBlank(message = "Zip code cannot be null or empty")
    private String zipCode;
    private String street;
    private String city;
    private String state;
    @Size(max = 10)
    @NotBlank(message = "Number cannot be null or empty")
    private String number;
    private String district;

}
