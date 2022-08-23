package br.com.compasso.client.dtos.request;

import br.com.compasso.client.Enums.Status;
import br.com.compasso.client.entities.Address;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.Email;

@Data
public class RequestClientDTO {
    @CPF
    @Id
    private String cpf;
    private String name;
    private DateTimeFormat birthDate;
    private Address address;
    @Email
    private String email;
    private Status status;
}
