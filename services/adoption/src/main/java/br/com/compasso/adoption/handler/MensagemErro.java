package br.com.compasso.adoption.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MensagemErro {

    private String status;
    private String mensagem;

    public MensagemErro(String mensagem) {
        this.mensagem = mensagem;
    }
}
