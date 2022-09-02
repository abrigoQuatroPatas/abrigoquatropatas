package br.com.compasso.adoption.service;

import br.com.compasso.adoption.dto.request.RequestEmailDto;
import br.com.compasso.adoption.dto.response.ResponseConsumerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${custom.rabbit-email-queue}")
    private String emailQueue;

    public void sendEmailToQueue(ResponseConsumerDto consumer) {
        RequestEmailDto email = new RequestEmailDto(consumer.getName(), consumer.getEmail());
        sendEmailMessage(email);
    }

    private void sendEmailMessage(RequestEmailDto email) {
        try {
            rabbitTemplate.convertAndSend(emailQueue, email);
        } catch (Exception e) {
            log.error("sendEmailMessage() - Erro to send email to rabbit: {}", e.getMessage());
        }
    }
}
