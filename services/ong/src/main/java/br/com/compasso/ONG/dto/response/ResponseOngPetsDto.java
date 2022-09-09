package br.com.compasso.ONG.dto.response;

import br.com.compasso.ONG.entity.Address;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseOngPetsDto {

    private String cnpj;
    private String name;
    private LocalDate foundationDate;
    private Address address;
    private Integer amountCat;
    private Integer amountDog;
    private List<ResponsePetDto> petIds = new ArrayList<>();
}
