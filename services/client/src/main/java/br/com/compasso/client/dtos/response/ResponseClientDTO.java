package br.com.compasso.client.dtos.response;

import br.com.compasso.client.Enums.Status;
import br.com.compasso.client.entities.Address;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
@Data
public class ResponseClientDTO {
    private String cpf;
    private String name;
    private DateTimeFormat birthDate;
    private Address address;
    @Email
    private String email;
    private Status status;
}
