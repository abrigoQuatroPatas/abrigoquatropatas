package br.com.compasso.ONG.dto.response;

import br.com.compasso.ONG.entity.Address;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseOngDto {

    private String Cnpj;
    private String name;
    private LocalDate foundationDate;
    private ResponseAddressDto address;
    private Integer amountCat;
    private Integer amountDog;

}
