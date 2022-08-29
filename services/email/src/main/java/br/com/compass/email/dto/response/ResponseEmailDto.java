package br.com.compass.email.dto.response;

import lombok.Data;

@Data
public class ResponseEmailDto {

    private String nome;
    private String email;
    private final String subject = "Adoption";
    private final String content = "Fill out the form below to complete your adoption process.";

}
