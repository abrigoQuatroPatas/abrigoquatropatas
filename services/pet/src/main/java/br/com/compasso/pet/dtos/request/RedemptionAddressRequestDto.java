package br.com.compasso.pet.dtos.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RedemptionAddressRequestDto {

    private String state;

    private String city;

    private String district;

    @NotBlank(message = "The zipCode field cannot be left blank")
    private String zipCode;

    private String street;

    @Size(max = 10)
    @NotBlank(message = "The number field cannot be left blank")
    private String number;

    @NotNull(message = "The redemptionDate field cannot be left null")
    private LocalDateTime redemptionDate;
}
