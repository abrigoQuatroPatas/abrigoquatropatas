package br.com.compasso.voluntary;

import br.com.compasso.voluntary.dto.request.RequestAddressDto;
import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.entity.VoluntaryEntity;
import br.com.compasso.voluntary.enums.StatusEnum;
import br.com.compasso.voluntary.enums.TypeEnum;
import br.com.compasso.voluntary.repository.VoluntaryRepository;
import br.com.compasso.voluntary.service.VoluntaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class VoluntaryServiceTest {

    @InjectMocks
    private VoluntaryService service;
    @Mock
    private VoluntaryRepository repository;
    @Mock
    private ModelMapper modelMapper;
    private VoluntaryEntity entity;
    private RequestVoluntaryDto voluntaryDto;

    @BeforeEach
    public void setUp() {
        this.entity = new VoluntaryEntity();
        this.entity.setCpf("67351300062");

        RequestAddressDto addressDto = RequestAddressDto.builder()
                .zipCode("93218-150")
                .street("Rua Doutor Otávio Azambuja")
                .city("Sapucaia do Sul")
                .state("RS")
                .number("21")
                .district("Freitas")
                .build();

        this.voluntaryDto = RequestVoluntaryDto.builder()
                .cpf("67351300062")
                .name("cpf teste")
                .type(TypeEnum.HELPER)
                .birthDate(LocalDate.now())
                .address(addressDto)
                .status(StatusEnum.AVAILABLE)
                .build();
    }

    @Test
    void post() {
        Mockito.when(modelMapper.map(this.voluntaryDto, VoluntaryEntity.class)).thenReturn(this.entity);

        service.post(this.voluntaryDto);
        Mockito.verify(repository).save(this.entity);
    }

    @Test
    void get() {
        Mockito.when(repository.findById("67351300062")).thenReturn(Optional.ofNullable(this.entity));

        service.get("67351300062");
        Mockito.verify(repository).findById(this.voluntaryDto.getCpf());
    }

    @Test
    void update() {
        Mockito.when(repository.findById("67351300062")).thenReturn(Optional.ofNullable(this.entity));

        service.update("67351300062", this.voluntaryDto);
        Mockito.verify(repository).save(this.entity);
    }

    @Test
    void delete() {
        Mockito.when(repository.findById("67351300062")).thenReturn(Optional.ofNullable(this.entity));

        service.delete("67351300062");
        Mockito.verify(repository).delete(this.entity);
    }
}
