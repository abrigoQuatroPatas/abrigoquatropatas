package br.com.compasso.pet.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;


import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedemptionAddressEntity {

    private String state;

    private String city;

    private String district;

    private String zipCode;

    private String street;

    private String number;

    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
    private LocalDateTime redemptionDate;
}
