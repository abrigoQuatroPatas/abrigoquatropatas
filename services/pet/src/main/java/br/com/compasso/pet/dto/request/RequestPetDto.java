package br.com.compasso.pet.dto.request;

import br.com.compasso.pet.enums.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RequestPetDto {

    private String id;

    @NotBlank(message = "The name field cannot be left blank")
    private String name;

    @NotNull(message = "The type field must be filled in with one of the available options: cat or dog")
    private Type type;

    @NotNull(message = "The arrivalDate field cannot be left null")
    private LocalDate arrivalDate;

    @Valid
    private RequestRedemptionAddressDto redemptionAddress;
}
