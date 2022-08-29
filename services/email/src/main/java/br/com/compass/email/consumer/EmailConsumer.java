package br.com.compass.email.consumer;

import br.com.compass.email.config.RabbitMqConfig;
import br.com.compass.email.dto.response.ResponseEmailDto;
import br.com.compass.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService service;

    @RabbitListener(queues = RabbitMqConfig.QUEUE)
    public void listenerEmailQueue(@Payload ResponseEmailDto emailDto) {
        service.sendEmail(emailDto);
    }

}
