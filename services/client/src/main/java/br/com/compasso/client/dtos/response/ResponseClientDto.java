package br.com.compasso.client.dtos.response;

import br.com.compasso.client.entitys.Address;
import br.com.compasso.client.enums.StatusEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseClientDto {

    private String cpf;
    private String name;
    private LocalDate birthDate;
    private Address address;
    private String email;
    private StatusEnum statusEnum;
}
