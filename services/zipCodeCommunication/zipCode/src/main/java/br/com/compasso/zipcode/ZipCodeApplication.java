package br.com.compasso.zipcode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ZipCodeApplication {

	public static void main(String[] args) {

		SpringApplication.run(ZipCodeApplication.class, args);
	}

}
