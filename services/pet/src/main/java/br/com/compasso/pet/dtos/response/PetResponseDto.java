package br.com.compasso.pet.dtos.response;

import br.com.compasso.pet.enums.TypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PetResponseDto {

    private String id;

    private String name;

    private TypeEnum type;

    private LocalDate arrivalDate;

    private RedemptionAddressResponseDto redemptionAddress;
}
