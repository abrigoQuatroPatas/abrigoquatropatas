package br.com.compasso.zipcode.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Setter
@Getter
public class ZipCodeResponse {

    private String uf;

    private String localidade;

    private String bairro;

    private String logradouro;
}
