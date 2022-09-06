package br.com.compasso.voluntary.entity;

import br.com.compasso.voluntary.enums.Status;
import br.com.compasso.voluntary.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class VoluntaryEntity {

    @Id
    private String cpf;
    private String name;
    private Type type;
    private LocalDate birthDate;
    private AddressEntity address;
    private Status status;
    private String ongId;

}
