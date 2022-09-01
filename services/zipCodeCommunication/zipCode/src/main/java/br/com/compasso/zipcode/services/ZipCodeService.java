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
        webClient = builder.baseUrl("https://viacep.com.br/ws").build(); //url base de quem eu quero me comunicar
    }

    public Mono<ZipCodeResponse> findZipCodeByCep (String cep){
        log.info("Looking for zip code {}", cep);

        return webClient
                .get()
                .uri("/{cep}/json/", cep)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus:: is4xxClientError,
                        error -> Mono.error(new RuntimeException("Verify cep")))
                .bodyToMono(ZipCodeResponse.class);
    }
}