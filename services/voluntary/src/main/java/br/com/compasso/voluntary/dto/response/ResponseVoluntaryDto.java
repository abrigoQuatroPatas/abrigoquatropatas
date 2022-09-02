package br.com.compasso.voluntary.dto.response;

import br.com.compasso.voluntary.entity.AddressEntity;
import br.com.compasso.voluntary.enums.StatusEnum;
import br.com.compasso.voluntary.enums.TypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseVoluntaryDto {

    private String cpf;
    private String name;
    private TypeEnum type;
    private LocalDate birthDate;
    private AddressEntity address;
    private StatusEnum status;
}