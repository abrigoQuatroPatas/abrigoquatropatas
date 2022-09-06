package br.com.compasso.pet.dtos.response;

import br.com.compasso.pet.enums.TypeEnum;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetResponseDto {

    private String id;

    private String name;

    private TypeEnum type;

    private LocalDate arrivalDate;

    private RedemptionAddressResponseDto redemptionAddress;

    private ResponseOngDto ong;
}
