package br.com.compasso.voluntary.exceptions;

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
public class ErrorMessage {

    private String status;
    private String msg;

    public ErrorMessage(String mensagemsgm) {
        this.msg = msg;
    }

}
