package br.com.compass.email.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfig {

    public static final  String EXCHANGE = "email.Exchange";
    public static final  String QUEUE = "email.Queue";
    public static final  String ROUTING_KEY = "email.RoutingKey";
    private final ConnectionFactory connectionFactory;

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @PostConstruct
    public void createRabbitElements() {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        exchange(rabbitAdmin);
        queue(rabbitAdmin);
    }

    private void exchange(RabbitAdmin admin) {
        Exchange exchange = ExchangeBuilder
                .directExchange(EXCHANGE)
                .durable(true)
                .build();

        admin.declareExchange(exchange);
    }

    private void queue(RabbitAdmin admin) {
        Queue queue = QueueBuilder
                .durable(QUEUE)
                .build();

        Binding binding = new  Binding(
                QUEUE,
                Binding.DestinationType.QUEUE,
                EXCHANGE,
                ROUTING_KEY,
                null
        );
        admin.declareQueue(queue);
        admin.declareBinding(binding);
    }

}
