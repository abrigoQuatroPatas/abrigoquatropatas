package com.compass.volunteer;

import com.compass.volunteer.dto.request.RequestAddressDto;
import com.compass.volunteer.dto.request.RequestVolunteerDto;
import com.compass.volunteer.entity.VolunteerEntity;
import com.compass.volunteer.enums.StatusEnum;
import com.compass.volunteer.enums.TypeEnum;
import com.compass.volunteer.repository.VolunteerRepository;
import com.compass.volunteer.service.VolunteerService;
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
public class VolunteerServiceTest {

    @InjectMocks
    private VolunteerService service;
    @Mock
    private VolunteerRepository repository;
    @Mock
    private ModelMapper modelMapper;
    private VolunteerEntity entity;
    private RequestVolunteerDto volunteerDto;

    @BeforeEach
    public void setUp() {
        this.entity = new VolunteerEntity();
        this.entity.setCpf("67351300062");

        RequestAddressDto addressDto = RequestAddressDto.builder()
                .zipCode("93218-150")
                .street("Rua Doutor Otávio Azambuja")
                .city("Sapucaia do Sul")
                .state("RS")
                .number("21")
                .district("Freitas")
                .build();

        this.volunteerDto = RequestVolunteerDto.builder()
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
        Mockito.when(modelMapper.map(this.volunteerDto, VolunteerEntity.class)).thenReturn(this.entity);

        service.post(this.volunteerDto);
        Mockito.verify(repository).save(this.entity);
    }

    @Test
    void get() {
        Mockito.when(repository.findById("67351300062")).thenReturn(Optional.ofNullable(this.entity));

        service.get("67351300062");
        Mockito.verify(repository).findById(this.volunteerDto.getCpf());
    }

    @Test
    void update() {
        Mockito.when(repository.findById("67351300062")).thenReturn(Optional.ofNullable(this.entity));

        service.update("67351300062", this.volunteerDto);
        Mockito.verify(repository).save(this.entity);
    }

    @Test
    void delete() {
        Mockito.when(repository.findById("67351300062")).thenReturn(Optional.ofNullable(this.entity));

        service.delete("67351300062");
        Mockito.verify(repository).delete(this.entity);
    }
}
