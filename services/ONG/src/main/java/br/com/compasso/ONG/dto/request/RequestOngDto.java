package br.com.compasso.ONG.dto.request;

import br.com.compasso.ONG.entity.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@Builder
public class RequestOngDto {

    @CNPJ
    private String Cnpj;
    @NotBlank
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate foundationDate;
    @NotNull
    private RequestAddressDto address;
    @NotNull
    @Positive
    private Integer amountCat;
    @NotNull
    @Positive
    private Integer amountDog;

}
