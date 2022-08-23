package br.com.compasso.ONG.service;

import br.com.compasso.ONG.dto.request.RequestAddressDto;
import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.entity.OngEntity;
import br.com.compasso.ONG.repository.OngRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class OngServiceTest {

    @InjectMocks
    private OngService service;
    @Mock
    private OngRepository repository;
    @Mock
    private ModelMapper modelMapper;
    private OngEntity ongEntity;
    private RequestOngDto ongDto;

    @BeforeEach
    public void setUp() {
        this.ongEntity = new OngEntity();
        this.ongEntity.setCnpj("45984180000121");

        RequestAddressDto addressDto = RequestAddressDto.builder()
                .zipCode("26041-048")
                .street("Rua Vidal")
                .city("Nova Iguaçu")
                .state("RJ")
                .number("35")
                .district("Parque Três Corações")
                .build();

        this.ongDto = RequestOngDto.builder()
                .Cnpj("45984180000121")
                .name("Teste")
                .foundationDate(LocalDate.now())
                .address(addressDto)
                .amountCat(5)
                .amountDog(10)
                .build();
    }

    @Test
    void post() {
        Mockito.when(modelMapper.map(this.ongDto, OngEntity.class)).thenReturn(this.ongEntity);

        service.post(this.ongDto);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @Test
    void get() {
        Mockito.when(repository.findById("45984180000121")).thenReturn(Optional.ofNullable(this.ongEntity));

        service.get("45984180000121");
        Mockito.verify(repository).findById(this.ongDto.getCnpj());
    }

    @Test
    void update() {
        Mockito.when(repository.findById("45984180000121")).thenReturn(Optional.ofNullable(this.ongEntity));

        service.update("45984180000121", this.ongDto);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @Test
    void delete() {
        Mockito.when(repository.findById("45984180000121")).thenReturn(Optional.ofNullable(this.ongEntity));

        service.delete("45984180000121");
        Mockito.verify(repository).delete(this.ongEntity);
    }
}