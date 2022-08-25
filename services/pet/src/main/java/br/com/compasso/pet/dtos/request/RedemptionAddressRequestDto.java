package br.com.compasso.pet.dtos.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class RedemptionAddressRequestDto {

    @NotBlank(message = "The state field cannot be left blank")
    private String state;

    @NotBlank(message = "The city field cannot be left blank")
    private String city;

    @NotBlank(message = "The district field cannot be left blank")
    private String district;

    @NotBlank(message = "The zipCode field cannot be left blank")
    private String zipCode;

    @NotBlank(message = "The street field cannot be left blank")
    private String street;

    @NotBlank(message = "The number field cannot be left blank")
    private String number;

    @NotNull(message = "The redemptionDate field cannot be left null")
    private LocalDateTime redemptionDate;

}
