package br.com.compasso.client.service;

import br.com.compasso.client.dto.request.RequestAddressDto;
import br.com.compasso.client.dto.request.RequestClientDto;
import br.com.compasso.client.entity.ClientEntity;
import br.com.compasso.client.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

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
    private ClientEntity clientEntity;
    private RequestClientDto clientDto;

    @BeforeEach
    public void setUp() {
        this.clientEntity = new ClientEntity();
        this.clientEntity.setCpf("42612289046");

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

    @Test
    void post() {
        Mockito.when(modelMapper.map(this.clientDto, ClientEntity.class)).thenReturn(this.clientEntity);

        service.post(this.clientDto);
        Mockito.verify(repository).save(this.clientEntity);
    }

    @Test
    void postException() {
        Mockito.when(modelMapper.map(this.clientDto, ClientEntity.class)).thenReturn(this.clientEntity);

        service.post(this.clientDto);
        Mockito.verify(repository).save(this.clientEntity);
    }

    @Test
    void get() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));

        service.get("42612289046");
        Mockito.verify(repository).findById(this.clientDto.getCpf());
    }

    @Test
    void getException() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.get("4261228904"));
    }

    @Test
    void update() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));

        service.update("42612289046", this.clientDto);
        Mockito.verify(repository).save(this.clientEntity);
    }

    @Test
    void updateException() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.update("53934042040", this.clientDto));
    }

    @Test
    void delete() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));

        service.delete("42612289046");
        Mockito.verify(repository).delete(this.clientEntity);
    }

    @Test
    void deleteException() {
        Mockito.when(repository.findById("42612289046")).thenReturn(Optional.ofNullable(this.clientEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.delete("53934042040"));
    }
}