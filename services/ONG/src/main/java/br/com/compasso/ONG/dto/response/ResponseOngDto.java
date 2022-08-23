package br.com.compasso.ONG.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseOngDto {

    private String Cnpj;
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate foundationDate;
    private ResponseAddressDto address;
    private Integer amountCat;
    private Integer amountDog;

}
