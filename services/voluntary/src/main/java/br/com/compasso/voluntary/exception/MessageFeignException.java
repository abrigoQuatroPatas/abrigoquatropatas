package br.com.compasso.voluntary.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageFeignException extends RuntimeException{

    private String status;
    private String msg;

}
