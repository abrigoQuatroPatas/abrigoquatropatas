package br.com.compasso.voluntary;

import br.com.compasso.voluntary.dto.request.RequestAddressDto;
import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.request.RequestVoluntaryPutDto;
import br.com.compasso.voluntary.dto.response.ResponseOngDto;
import br.com.compasso.voluntary.entity.VoluntaryEntity;
import br.com.compasso.voluntary.enums.StatusEnum;
import br.com.compasso.voluntary.enums.TypeEnum;
import br.com.compasso.voluntary.http.OngClient;
import br.com.compasso.voluntary.httpclient.ZipCodeClient;
import br.com.compasso.voluntary.repository.VoluntaryRepository;
import br.com.compasso.voluntary.response.ZipCodeResponse;
import br.com.compasso.voluntary.service.VoluntaryService;
import feign.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class VoluntaryServiceTest {

    @InjectMocks
    private VoluntaryService service;
    @Mock
    private VoluntaryRepository repository;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private OngClient ongClient;
    @Mock
    private ZipCodeClient zipCodeClient;
    private VoluntaryEntity entity;
    private RequestVoluntaryDto voluntaryDto;
    private ResponseOngDto responseOngDto;
    private RequestVoluntaryPutDto voluntaryPutDto;
    private Mono<ZipCodeResponse> zipCodeResponse;



    @BeforeEach
    public void setUp() {
        this.entity = new VoluntaryEntity();
        this.entity.setCpf("88689769196");

        responseOngDto = new ResponseOngDto();
        responseOngDto.setCnpj("40253428000160");
        responseOngDto.setName("Teste Ong post voluntary");

        ZipCodeResponse zipCode = new ZipCodeResponse();
        zipCode.setCity("Salvador");
        zipCode.setState("BA");
        zipCode.setStreet("Avenida Cardeal Da Silva");
        zipCode.setDistrict("Federação");

        this.zipCodeResponse = Mono.just(zipCode);

        RequestAddressDto addressDto = RequestAddressDto.builder()
                .zipCode("24716012")
                .street("Rua Expedicionário Antenor Costa")
                .city("São Gonçalo")
                .state("RJ")
                .number("194")
                .district("Jardim Catarina")
                .build();

        this.voluntaryDto = RequestVoluntaryDto.builder()
                .cpf("88689769196")
                .name("cpf teste")
                .type(TypeEnum.HELPER)
                .birthDate(LocalDate.now())
                .address(addressDto)
                .status(StatusEnum.AVAILABLE)
                .build();
    }

    @DisplayName("Deveria cadastrar um voluntary")
    @Test
    void post() {
        Mockito.when(modelMapper.map(this.voluntaryDto, VoluntaryEntity.class)).thenReturn(this.entity);
        Mockito.when(repository.existsById(voluntaryDto.getCpf())).thenReturn(false);
        Mockito.when(zipCodeClient.findAddressByVolunteeer(Mockito.any())).thenReturn(zipCodeResponse);
        service.post(this.voluntaryDto, this.entity.getOngId());
        Mockito.verify(repository).save(this.entity);
    }

    @DisplayName("Deveria retornar um voluntary pelo id")
    @Test
    void get() {
        Mockito.when(repository.findById("88689769196")).thenReturn(Optional.ofNullable(this.entity));
        Mockito.when(ongClient.getOng(Mockito.anyString())).thenReturn(responseOngDto);
        service.get("88689769196");
        Mockito.verify(repository).findById(this.voluntaryDto.getCpf());
    }

    @DisplayName("Deveria retornar exceção no getById")
    @Test
    void getException() {
        Mockito.when(repository.findById("88689769196")).thenReturn(Optional.ofNullable(this.entity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.get("886897691"));
    }

    @DisplayName("Deveria atualizar um voluntary")
    @Test
    void update() {
        Mockito.when(repository.findById("88689769196")).thenReturn(Optional.ofNullable(this.entity));

        service.update("88689769196", this.voluntaryPutDto);
        Mockito.verify(repository).save(this.entity);
    }

    @DisplayName("Deveria retornar exceção no update")
    @Test
    void updateException() {
        Mockito.when(repository.findById("88689769196")).thenReturn(Optional.ofNullable(this.entity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.update("53934042040", this.voluntaryPutDto));
    }

    @DisplayName("Deveria deletar um voluntary")
    @Test
    void delete() {
        Mockito.when(repository.findById("88689769196")).thenReturn(Optional.ofNullable(this.entity));
        service.delete("88689769196");
        Mockito.verify(repository).delete(this.entity);
    }

    @DisplayName("Deveria retornar exception ao deletar por id")
    @Test
    void deleteException() {
        Mockito.when(repository.findById("88689769196")).thenReturn(Optional.ofNullable(this.entity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.delete("53934042040"));
    }
}
