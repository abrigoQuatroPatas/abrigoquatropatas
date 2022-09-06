package br.com.compasso.client.dto.response;

import br.com.compasso.client.entity.Address;
import br.com.compasso.client.enums.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseClientDto {

    private String cpf;
    private String name;
    private LocalDate birthDate;
    private Address address;
    private String email;
    private Status status;
}
