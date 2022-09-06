package br.com.compasso.voluntary.entity;

import br.com.compasso.voluntary.enums.StatusEnum;
import br.com.compasso.voluntary.enums.TypeEnum;
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
    private TypeEnum type;
    private LocalDate birthDate;
    private AddressEntity address;
    private StatusEnum status;
    private String ongId;

}
