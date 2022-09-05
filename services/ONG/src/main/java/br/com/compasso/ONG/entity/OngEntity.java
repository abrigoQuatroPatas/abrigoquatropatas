package br.com.compasso.ONG.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OngEntity {

    @Id
    private String cnpj;
    private String name;
    private LocalDate foundationDate;
    private Address address;
    private Integer amountCat;
    private Integer amountDog;
    private List<String> voluntaryIds = new ArrayList<>();
}
