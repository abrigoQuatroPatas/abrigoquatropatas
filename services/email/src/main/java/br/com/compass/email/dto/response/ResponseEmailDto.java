package br.com.compass.email.dto.response;

import lombok.Data;

@Data
public class ResponseEmailDto {

    private String name;
    private String email;
    private final String subject = "Adoption";
    private final String content = "Fill out the form below to complete your adoption process.\nLink: https://forms.gle/dV7k3YzDjQmweTxq8";

}
