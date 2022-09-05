package br.com.compasso.voluntary.dto.request;

import br.com.compasso.voluntary.enums.StatusEnum;
import br.com.compasso.voluntary.enums.TypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestVoluntaryDto {

    @CPF
    private String cpf;

    @NotBlank(message = "Name field cannot be blank")
    private String name;

    @NotNull(message = "Type field cannot be blank")
    private TypeEnum type;

    @JsonFormat(pattern="dd/MM/yyyy")
    @NotNull(message = "BirthDate field cannot be null")
    private LocalDate birthDate;

    @Valid
    private RequestAddressDto address;

    @NotNull(message = "Status field cannot be blank")
    private StatusEnum status;
}
