package br.com.compass.email.service;

import br.com.compass.email.dto.response.ResponseEmailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {


    @Autowired
    JavaMailSender mailSender;

    public void sendEmail(ResponseEmailDto emailDto) {
        log.info("sendEmail() - start - sending email for {}, for email {}", emailDto.getName(), emailDto.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("abrigo.quatro.patas@gmail.com");
        message.setTo(emailDto.getEmail());
        message.setSubject(emailDto.getSubject());
        message.setText(emailDto.getContent());
        mailSender.send(message);

    }

}
