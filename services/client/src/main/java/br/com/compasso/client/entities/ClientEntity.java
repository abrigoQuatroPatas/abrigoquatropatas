package br.com.compasso.client.entities;

import br.com.compasso.client.Enums.Status;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {

    @Id
    @CPF
    private String cpf;
    private String name;
    private DateTimeFormat birthDate;
    private Address address;
    @Email
    private String email;
    private Status status;
}
