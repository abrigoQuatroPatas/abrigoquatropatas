package br.com.compasso.voluntary.dto.response;

import br.com.compasso.voluntary.enums.Status;
import br.com.compasso.voluntary.enums.Type;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVoluntaryDto {

    private String cpf;
    private String name;
    private Type type;
    private LocalDate birthDate;
    private Status status;
    private ResponseAddressDto address;
    private ResponseOngDto ong;
}
