package br.com.compasso.adoption.handler;

import br.com.compasso.adoption.exception.MessageFeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(value = ResponseStatusException.class)
    protected ResponseEntity<MensagemErro> handlerClientJaCadastrado(ResponseStatusException exception) {
        if (exception.getStatus() == HttpStatus.OK){
            return ResponseEntity.status(HttpStatus.OK).body(new MensagemErro("already existing an adoption linked to these objects."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErro("adoption not found."));
    }

    @ExceptionHandler(value = MessageFeignException.class)
    protected ResponseEntity<MensagemErro> handlerClientJaCadastrado(MessageFeignException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErro(exception.getStatus(), exception.getMessage()));
    }

}
