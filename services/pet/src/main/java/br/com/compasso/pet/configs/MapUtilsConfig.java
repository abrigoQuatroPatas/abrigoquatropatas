package br.com.compasso.pet.configs;

import br.com.compasso.pet.utils.MapUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapUtilsConfig {

    @Bean
    public MapUtils MapUtils() {

        return new MapUtils();
    }
}
