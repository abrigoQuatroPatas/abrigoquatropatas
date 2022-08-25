package br.com.compasso.voluntary.dto.request;

import com.compass.volunteer.entity.AddressEntity;
import com.compass.volunteer.enums.StatusEnum;
import com.compass.volunteer.enums.TypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RequestVoluntaryDto {

    @CPF
    private String cpf;
    @NotBlank
    private String name;
    @Valid
    private TypeEnum type;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @Valid
    private RequestAddressDto address;
    @Valid
    private StatusEnum status;
}
