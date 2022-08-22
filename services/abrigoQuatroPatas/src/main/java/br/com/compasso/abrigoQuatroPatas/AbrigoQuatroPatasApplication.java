package br.com.compasso.abrigoQuatroPatas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AbrigoQuatroPatasApplication {

	public static void main(String[] args) {
		SpringApplication.run(AbrigoQuatroPatasApplication.class, args);
	}

}
