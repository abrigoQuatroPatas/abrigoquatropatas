package br.com.compasso.ONG;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class OngApplication {

	public static void main(String[] args) {
		SpringApplication.run(OngApplication.class, args);
	}

}
