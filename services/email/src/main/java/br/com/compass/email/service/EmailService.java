package br.com.compass.email.service;

import br.com.compass.email.dto.response.ResponseEmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    public void sendEmail(ResponseEmailDto emailDto) {
        log.info("sendEmail() - start - sending email for {}, for email {}", emailDto.getNome(), emailDto.getEmail());
    }

}
