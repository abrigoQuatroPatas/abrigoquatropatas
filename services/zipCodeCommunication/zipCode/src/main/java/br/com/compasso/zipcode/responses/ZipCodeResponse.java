package br.com.compasso.zipcode.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ZipCodeResponse {

    private String uf;

    private String localidade;

    private String bairro;

    private String logradouro;
}
