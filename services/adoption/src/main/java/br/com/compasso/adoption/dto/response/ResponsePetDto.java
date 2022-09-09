package br.com.compasso.adoption.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePetDto {

    private String id;
    private String name;
    private String type;
    private ResponseOngDto ong;
}
