package br.com.compasso.pet.dto.response;

import br.com.compasso.pet.enums.Type;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponsePetDto {

    private String id;

    private String name;

    private Type type;

    private LocalDate arrivalDate;

    private ResponseRedemptionAddressDto redemptionAddress;

    private ResponseOngDto ong;
}
