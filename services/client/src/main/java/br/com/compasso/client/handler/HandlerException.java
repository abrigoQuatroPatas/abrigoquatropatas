package br.com.compasso.client.handler;

import br.com.compasso.client.exception.AddressNotFound;
import br.com.compasso.client.exception.NotStatusNull;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandlerException {

    @ExceptionHandler(value = ResponseStatusException.class)
    protected ResponseEntity<MensagemErro> handlerClientJaCadastrado(ResponseStatusException exception) {
        if (exception.getStatus() == HttpStatus.OK){
            return ResponseEntity.status(HttpStatus.OK).body(new MensagemErro("Cliente já existente."));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MensagemErro("Cliente não encontrado."));
    }

    @ExceptionHandler(value = NotStatusNull.class)
    protected ResponseEntity<MensagemErro> handlerStatusDiferenteDeNull(NotStatusNull exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new MensagemErro("Não é possível deletar um cliente com status não nulo."));
    }
    @ExceptionHandler(value = AddressNotFound.class)
    protected ResponseEntity<MensagemErro> handlerAddressNotFound (AddressNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new MensagemErro("Não é possível deletar um cliente com status não nulo."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MensagemErro> handleValidationExceptions(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errorMessage = new ArrayList<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        fieldErrors.forEach(ex -> {
            String msgError = "O campo '" + ex.getField() + "' " + ex.getDefaultMessage();
            errorMessage.add(msgError);
        });
        return ResponseEntity.status(status).body(new MensagemErro(errorMessage));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MensagemErro> handleHttpValidationExceptions(HttpMessageNotReadableException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMessage = "Invalid information";

        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) e.getCause();
            errorMessage = cause.getCause().getMessage();
        }
        return ResponseEntity.status(status).body(new MensagemErro(errorMessage));
    }
}
