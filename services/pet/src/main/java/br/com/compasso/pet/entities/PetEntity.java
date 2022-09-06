package br.com.compasso.pet.entities;

import br.com.compasso.pet.enums.TypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Document("pet")
public class PetEntity {

    @Id
    private String id;

    private String name;

    private TypeEnum type;

    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate arrivalDate;

    private RedemptionAddressEntity redemptionAddress;

    private String ongId;
}
