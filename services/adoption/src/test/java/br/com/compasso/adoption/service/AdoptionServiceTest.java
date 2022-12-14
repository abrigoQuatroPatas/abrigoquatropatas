package br.com.compasso.adoption.service;

import br.com.compasso.adoption.dto.response.ResponseAdoptionDto;
import br.com.compasso.adoption.dto.response.ResponseConsumerDto;
import br.com.compasso.adoption.dto.response.ResponseOngDto;
import br.com.compasso.adoption.dto.response.ResponsePetDto;
import br.com.compasso.adoption.entity.AdoptionEntity;
import br.com.compasso.adoption.http.ConsumerClient;
import br.com.compasso.adoption.http.OngClient;
import br.com.compasso.adoption.http.PetClient;
import br.com.compasso.adoption.repository.AdoptionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = AdoptionService.class)
@AutoConfigureMockMvc
class AdoptionServiceTest {

    @Autowired
    private AdoptionService service;

    @MockBean
    private RabbitService rabbitService;
    @MockBean
    private AdoptionRepository repository;
    @MockBean
    private ConsumerClient consumerClient;
    @MockBean
    private OngClient ongClient;
    @MockBean
    private PetClient petClient;
    private AdoptionEntity adoption;

    private ResponseConsumerDto consumerDto;
    private ResponseOngDto ongDto;
    private ResponsePetDto petDto;
    private ResponseAdoptionDto adoptionDto;

    @BeforeEach
    public  void setUp() {
        this.adoption = Mockito.mock(AdoptionEntity.class);

        this.consumerDto = ResponseConsumerDto.builder()
                .cpf("68395952007")
                .name("Teste")
                .email("test@gmail.com")
                .build();
        this.ongDto = ResponseOngDto.builder()
                .cnpj("75117292000146")
                .name("Ong Teste")
                .build();
        this.petDto = ResponsePetDto.builder()
                .id("63176c45f6fb020a1b9b84ba")
                .name("Pet Teste")
                .type("dog")
                .ong(ongDto)
                .build();

        this.adoptionDto = ResponseAdoptionDto.builder()
                .id(1L)
                .pet(petDto)
                .consumer(consumerDto)
                .adoptionDate(LocalDate.now())
                .build();


    }

    @DisplayName("Deveria realizar a ado????o de um pet")
    @Test
    void post() {
        Mockito.when(consumerClient.getConsumer(consumerDto.getCpf())).thenReturn(this.consumerDto);
        Mockito.when(petClient.getPet(petDto.getId())).thenReturn(this.petDto);
        Mockito.when(ongClient.getOng(ongDto.getCnpj())).thenReturn(this.ongDto);
        Mockito.when(repository.existsByPetId(petDto.getId())).thenReturn(false);

        ResponseAdoptionDto post = service.post("68395952007", "63176c45f6fb020a1b9b84ba");
        post.setId(1L);
        Assertions.assertNotNull(post);
        Assertions.assertEquals(post, this.adoptionDto);

    }

    @DisplayName("Deveria aprovar a ado????o de um pet")
    @Test
    void putStatusConsumerApproved() {
        service.putStatusConsumerApproved(this.consumerDto.getCpf());
        Mockito.verify(consumerClient).putStatusConsumerApproved(this.consumerDto.getCpf());
    }

    @DisplayName("Deveria rejeitar a ado????o de um pet")
    @Test
    void putStatusConsumerDisapproved() {
        service.putStatusConsumerDisapproved(this.consumerDto.getCpf());
        Mockito.verify(consumerClient).putStatusConsumerDisapproved(this.consumerDto.getCpf());
    }

    @DisplayName("Deveria pegar uma ado????o")
    @Test
    void get() {
        Mockito.when(repository.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(this.adoption));
        Mockito.when(consumerClient.getConsumer(adoption.getConsumerId())).thenReturn(this.consumerDto);
        Mockito.when(petClient.getPet(adoption.getPetId())).thenReturn(this.petDto);
        Mockito.when(ongClient.getOng(petDto.getOng().getCnpj())).thenReturn(this.ongDto);

        ResponseAdoptionDto responseAdoptionDto = service.get(1L);
        responseAdoptionDto.setId(1L);
        responseAdoptionDto.setAdoptionDate(LocalDate.now());
        Assertions.assertNotNull(responseAdoptionDto);
        Assertions.assertEquals(responseAdoptionDto, this.adoptionDto);
    }

    @DisplayName("Deveria pegar todas as ado????es")
    @Test
    void getAll() {
        Mockito.when(repository.findAll()).thenReturn(List.of(this.adoption));
        Mockito.when(consumerClient.getConsumer(adoption.getConsumerId())).thenReturn(this.consumerDto);
        Mockito.when(petClient.getPet(adoption.getPetId())).thenReturn(this.petDto);
        Mockito.when(ongClient.getOng(petDto.getOng().getCnpj())).thenReturn(this.ongDto);

        List<ResponseAdoptionDto> responseAdoptionDtos = service.get();
        Assertions.assertNotNull(responseAdoptionDtos);
    }

    @DisplayName("Deveria deletar uma ado????o")
    @Test
    void delete() {
        Mockito.when(repository.findById(ArgumentMatchers.eq(1L))).thenReturn(Optional.of(this.adoption));
        Mockito.when(petClient.getPet(adoption.getPetId())).thenReturn(this.petDto);

        service.delete(1L);
        Mockito.verify(ongClient).putAmountPetPlus(this.petDto.getOng().getCnpj(), this.petDto.getType());
    }
}