package br.com.compasso.voluntary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class VoluntaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoluntaryApplication.class, args);
	}

}
