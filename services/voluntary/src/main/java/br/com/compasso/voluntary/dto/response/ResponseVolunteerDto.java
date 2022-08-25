package com.compass.volunteer.dto.response;

import com.compass.volunteer.entity.AddressEntity;
import com.compass.volunteer.enums.StatusEnum;
import com.compass.volunteer.enums.TypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ResponseVolunteerDto {

    private String cpf;
    private String name;
    private TypeEnum type;
    private LocalDate birthDate;
    private AddressEntity address;
    private StatusEnum status;
}
