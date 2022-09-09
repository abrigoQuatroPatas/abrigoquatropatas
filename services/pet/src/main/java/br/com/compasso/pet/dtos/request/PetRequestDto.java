package br.com.compasso.pet.dtos.request;

import br.com.compasso.pet.enums.TypeEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PetRequestDto {

    private String id;

    @NotBlank(message = "The name field cannot be left blank")
    private String name;

    @NotNull(message = "The type field must be filled in with one of the available options: cat or dog")
    private TypeEnum type;

    @NotNull(message = "The arrivalDate field cannot be left null")
    private LocalDate arrivalDate;

    @Valid
    private RedemptionAddressRequestDto redemptionAddress;
}