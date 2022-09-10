package br.com.compasso.ONG.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestOngPutDto {

    @NotBlank
    private String name;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate foundationDate;
    @NotNull
    @Valid
    private RequestPutAddressDto address;
}
