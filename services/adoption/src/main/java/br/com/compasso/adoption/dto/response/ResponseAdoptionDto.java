package br.com.compasso.adoption.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAdoptionDto {

    private Long id;
    private ResponsePetDto pet;
    private ResponseConsumerDto consumer;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate adoptionDate;
}
