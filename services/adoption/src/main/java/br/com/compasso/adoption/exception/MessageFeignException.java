package br.com.compasso.adoption.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageFeignException extends RuntimeException {

    private String status;
    private String message;
}
