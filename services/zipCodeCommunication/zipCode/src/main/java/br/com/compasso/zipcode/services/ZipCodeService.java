package br.com.compasso.zipcode.services;

import br.com.compasso.zipcode.responses.ZipCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ZipCodeService {
    private final WebClient webClient;

    public ZipCodeService(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://viacep.com.br/ws").build();
    }

    public Mono<ZipCodeResponse> findZipCodeByCep (String zipCode){
        log.info("Looking for zip code {}", zipCode);

        return webClient
                .get()
                .uri("/{zipCode}/json/", zipCode)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus:: is4xxClientError,
                        error -> Mono.error(new RuntimeException("Verify zipCode")))
                .bodyToMono(ZipCodeResponse.class);
    }
}