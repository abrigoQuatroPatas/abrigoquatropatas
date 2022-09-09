package br.com.compasso.voluntary;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@OpenAPIDefinition(
		info = @Info(title = "Voluntary API", version = "0.1", description = "Voluntary API documentation")
)
public class VoluntaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoluntaryApplication.class, args);
	}

}
