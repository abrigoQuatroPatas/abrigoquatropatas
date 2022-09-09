package br.com.compasso.zipcode.service;

import br.com.compasso.zipcode.dto.response.ZipCodeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ZipCodeServiceTest {


    @InjectMocks
    ZipCodeService service;

    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.RequestBodySpec requestBodyMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findZipCodeByCep() {
        final String CEP = "59925000";
        final String UF = "RN", LOCALIDADE = "Venha-Ver";
        ZipCodeResponse zipCodeResponse = new ZipCodeResponse();
        zipCodeResponse.setUf(UF);
        zipCodeResponse.setLocalidade(LOCALIDADE);

        //when(WebClient.builder().baseUrl("https://viacep.com.br/ws").build()).thenReturn(this.webClientMock);

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("/{CEP}/json/", CEP)).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(ZipCodeResponse.class)).thenReturn(Mono.just(zipCodeResponse));

        Mono<ZipCodeResponse> r = service.findZipCodeByCep(CEP);
        ZipCodeResponse response = r.block();

        Assertions.assertEquals(LOCALIDADE, response.getLocalidade());
        Assertions.assertEquals(UF, response.getUf());
    }
}
