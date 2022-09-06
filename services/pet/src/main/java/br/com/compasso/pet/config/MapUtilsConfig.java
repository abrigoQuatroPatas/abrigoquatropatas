package br.com.compasso.pet.config;

import br.com.compasso.pet.util.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapUtilsConfig {

    @Bean
    public MapUtils MapUtils() {

        return new MapUtils();
    }
}
