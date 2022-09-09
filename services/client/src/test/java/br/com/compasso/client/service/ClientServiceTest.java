package br.com.compasso.client.service;

import br.com.compasso.client.dto.request.RequestAddressDto;
import br.com.compasso.client.dto.request.RequestClientDto;
import br.com.compasso.client.dto.request.RequestClientPutDto;
import br.com.compasso.client.dto.response.ZipCodeResponse;
import br.com.compasso.client.entity.ClientEntity;
import br.com.compasso.client.enums.Status;
import br.com.compasso.client.http.ZipCodeClient;
import br.com.compasso.client.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ClientServiceTest {
    @InjectMocks
    private ClientService service;
    @Mock
    private ClientRepository repository;
    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ZipCodeClient zipCodeClient;
    private ClientEntity clientEntity;
    private RequestClientDto clientDto;
    private RequestClientPutDto clientPutDto;

    private Mono<ZipCodeResponse> zipCodeResponse;

    @BeforeEach
    public void setUp() {
        this.clientEntity = new ClientEntity();
        this.clientEntity.setCpf("42612289046");
        this.clientEntity.setStatus(null);
        this.clientEntity.setName("Marcele");
        this.clientEntity.setEmail("marcele055@gmail.com");

        ZipCodeResponse zipCode = new ZipCodeResponse();
        zipCode.setCity("Salvador");
        zipCode.setState("BA");
        zipCode.setStreet("Avenida Cardeal Da Silva");
        zipCode.setDistrict("Federação");

        this.zipCodeResponse = Mono.just(zipCode);

        RequestAddressDto addressDto = RequestAddressDto.builder()
                .zipCode("26041-048")
                .street("Rua Vidal")
                .city("Nova Iguaçu")
                .state("RJ")
                .number("35")
                .district("Parque Três Corações")
                .build();

        this.clientDto = RequestClientDto.builder()
                .cpf("42612289046")
                .name("Teste")
                .birthDate(LocalDate.now())
                .address(addressDto)
                .email("fulano@gmail.com")
                .build();
    }

    @DisplayName("Deve cadastrar um cliente")
    @Test
    void post() {
        Mockito.when(modelMapper.map(this.clientDto, ClientEntity.class)).thenReturn(this.clientEntity);
        Mockito.when(repository.existsById(clientEntity.getCpf())).thenReturn(false);
        Mockito.when(zipCodeClient.findAddressByClient(Mockito.any())).thenReturn(zipCodeResponse);
        service.post(this.clientDto);
        Mockito.verify(repository).save(this.clientEntity);
    }

    @DisplayName("Deve retornar um cliente")
    @Test
    void get() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        service.get("42612289046");
        Mockito.verify(repository).findById(this.clientDto.getCpf());
    }

    @DisplayName("Deve devolver uma exceção caso não seja possível retornar um cliente")
    @Test
    void getException() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.get("4261228904"));
    }

    @DisplayName("Deve alterar um cliente")
    @Test
    void update() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        service.update("42612289046", this.clientPutDto);
        Mockito.verify(repository).save(this.clientEntity);
    }

    @DisplayName("Deve alterar o status do cliente para nulo")
    @Test
    void updateStatusNull() {
        this.clientEntity.setStatus(null);
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(clientEntity));
        service.putStatusNull("42612289046");
        Mockito.verify(repository).save(clientEntity);
    }
    @DisplayName("Deve alterar o status do cliente para 'em progresso'")
    @Test
    void updateStatusInProgress() {
        this.clientEntity.setStatus(Status.IN_PROGRESS);
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(clientEntity));
        service.putStatusInProgress("42612289046");
    }

    @DisplayName("Deve alterar o status do cliente para 'não aprovado'")
    @Test
    void updateStatusDisapproved() {
        this.clientEntity.setStatus(Status.DISAPPROVED);
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(clientEntity));
        service.putStatusDisapproved("42612289046");
    }

    @DisplayName("Deve alterar o status do cliente para 'aprovado'")
    @Test
    void updateStatusApproved() {
        this.clientEntity.setStatus(Status.APPROVED);
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(clientEntity));
        service.putStatusApproved("42612289046");
    }

    @DisplayName("Deve devolver uma exceção caso não seja possível alterar um cliente")
    @Test
    void updateException() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.update("53934042040", this.clientPutDto));
    }

    @DisplayName("Deve deletar um cliente")
    @Test
    void delete() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));

        service.delete("42612289046");
        Mockito.verify(repository).delete(this.clientEntity);
    }

    @DisplayName("Deve devolver uma exceção caso não seja possível deletar um cliente")
    @Test
    void deleteException() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.delete("53934042040"));
    }
}