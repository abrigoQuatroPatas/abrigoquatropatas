package br.com.compasso.ONG.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ZipCodeResponse {

    @JsonProperty("uf")
    private String state;

    @JsonProperty("localidade")
    private String city;

    @JsonProperty("bairro")
    private String district;

    @JsonProperty("logradouro")
    private String street;
}
