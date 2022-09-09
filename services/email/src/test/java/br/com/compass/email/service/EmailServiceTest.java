package br.com.compass.email.service;

import br.com.compass.email.dto.response.ResponseEmailDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
@AutoConfigureMockMvc
class EmailServiceTest {

    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    private EmailService service;

    @DisplayName("Deveria enviar um email")
    @Test
    void sendEmail() {
        ResponseEmailDto mock = Mockito.mock(ResponseEmailDto.class);
        service.sendEmail(mock);
    }
}