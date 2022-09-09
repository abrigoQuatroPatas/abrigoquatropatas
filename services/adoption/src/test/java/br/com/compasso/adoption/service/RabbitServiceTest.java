package br.com.compasso.adoption.service;

import br.com.compasso.adoption.dto.response.ResponseConsumerDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class RabbitServiceTest {

    @Autowired
    private RabbitService service;

    @DisplayName("Deveria enviar um email")
    @Test
    void sendEmailToQueue() {
        ResponseConsumerDto consumerDto = Mockito.mock(ResponseConsumerDto.class);
        service.sendEmailToQueue(consumerDto);
    }
}