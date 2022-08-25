package br.com.compasso.voluntary.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class Config {


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
