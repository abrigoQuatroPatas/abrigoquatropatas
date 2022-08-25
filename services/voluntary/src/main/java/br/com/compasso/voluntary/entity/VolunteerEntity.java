package com.compass.volunteer.entity;

import com.compass.volunteer.enums.StatusEnum;
import com.compass.volunteer.enums.TypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class VolunteerEntity {

    @Id
    private String cpf;
    private String name;
    private TypeEnum type;
    private LocalDate birthDate;
    private AddressEntity address;
    private StatusEnum status;

}
