package br.com.compasso.ONG.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ResponseOngVolunteersDto {

    private String Cnpj;
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate foundationDate;
    private ResponseAddressDto address;
    private Integer amountCat;
    private Integer amountDog;
    private List<ResponseVoluntaryDto> voluntaries;
}
