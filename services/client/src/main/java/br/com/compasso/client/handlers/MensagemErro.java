package br.com.compasso.client.handlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MensagemErro {
    private String mensagem;
    private List<String> validadorDeErros;

    public MensagemErro(List<String> validadorDeErros) {
        this.validadorDeErros = validadorDeErros;
    }

    public MensagemErro(String mensagem) {
        this.mensagem = mensagem;
    }
}

