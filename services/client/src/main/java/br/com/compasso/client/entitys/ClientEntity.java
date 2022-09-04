package br.com.compasso.client.entitys;

import br.com.compasso.client.enums.StatusEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Builder
@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientEntity {

    @Id
    private String cpf;
    private String name;
    private LocalDate birthDate;
    private Address address;
    private String email;
    private StatusEnum statusEnum;

}
