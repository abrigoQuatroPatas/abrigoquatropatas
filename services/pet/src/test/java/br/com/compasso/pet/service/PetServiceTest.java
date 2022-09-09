package br.com.compasso.pet.service;

import br.com.compasso.pet.dto.request.RequestPetDto;
import br.com.compasso.pet.dto.request.RequestRedemptionAddressDto;
import br.com.compasso.pet.dto.response.*;
import br.com.compasso.pet.entity.PetEntity;
import br.com.compasso.pet.enums.Type;
import br.com.compasso.pet.http.AdoptionClient;
import br.com.compasso.pet.http.OngClient;
import br.com.compasso.pet.http.ZipCodeClient;
import br.com.compasso.pet.repository.PetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class PetServiceTest {

    @InjectMocks
    private PetService service;
    @Mock
    private PetRepository repository;
    @Mock
    private ModelMapper modelMapper;
    private PetEntity petEntity;
    private RequestPetDto requestPetDto;

    @Mock
    private OngClient ong;

    @Mock
    private AdoptionClient adoption;

    @Mock
    private ZipCodeClient zipCodeClient;

    @Mock
    private WebTestClient webClient;

    private RequestPetDto petDto;
    private Mono<ZipCodeResponse> zipCodeResponse;

    @BeforeEach
    public void initializePet() {
        this.petEntity = new PetEntity();
        this.petEntity.setId("123456");
        this.petEntity.setOngId("83240333000115");

        ZipCodeResponse zipCode = new ZipCodeResponse();
        zipCode.setState("SC");
        zipCode.setCity("Florianópolis");
        zipCode.setDistrict("Armação do Pântano do Sul");
        zipCode.setStreet("Rodovia Francisco Thomaz dos Santos");

        this.zipCodeResponse = Mono.just(zipCode);

        RequestRedemptionAddressDto redemptionAddress = RequestRedemptionAddressDto.builder()
                .zipCode("88066-260")
                .state("SC")
                .city("Florianópolis")
                .district("Armação do Pântano do Sul")
                .street("Rodovia Francisco Thomaz dos Santos")
                .number("4400")
                .redemptionDate(LocalDateTime.now())
                .build();

        this.petDto = RequestPetDto.builder()
                .id("123456")
                .name("Pet Teste")
                .arrivalDate(LocalDate.now())
                .type(Type.CAT)
                .redemptionAddress(redemptionAddress)
                .build();
    }

    @DisplayName("Must register a pet and associate it with an ong")
    @Test
    void postPet() {
        Mockito.when(modelMapper.map(this.petDto, PetEntity.class)).thenReturn(this.petEntity);
        Mockito.when(repository.existsById(petEntity.getId())).thenReturn(false);
        Mockito.when(zipCodeClient.findRedemptionAddressByPet(Mockito.any())).thenReturn(zipCodeResponse);
        service.postPet(this.petDto, this.petEntity.getOngId());
        Mockito.verify(repository).save(this.petEntity);
    }

    @DisplayName("Must show all pets")
    @Test
    void getAllPets() {
        PageRequest pagination = PageRequest.of(1, 10);

        List<PetEntity> ong = Arrays.asList(new PetEntity());
        Page<PetEntity> ongPage = new PageImpl<>(ong, pagination, ong.size());

        List<ResponsePetDto> ordersDto = Arrays.asList(new ResponsePetDto());
        Page<ResponsePetDto> pageDto = new PageImpl<>(ordersDto, pagination, ordersDto.size());

        Mockito.when(repository.findAll(pagination)).thenReturn(ongPage);

        Page<ResponsePetDto> res = service.getAllPets(pagination);
        Assertions.assertTrue(res.getContent().size() == 1);
    }

    @DisplayName("Must show a specific pet by his id")
    @Test
    void getPet() {
        final String ID = "123456";
        ResponseOngDto ongDto = new ResponseOngDto();
        Mockito.when(repository.findById(ID)).thenReturn(Optional.ofNullable(this.petEntity));
        Mockito.when(ong.getOng(ID)).thenReturn(ongDto);

        ResponsePetDto res = service.getPet(ID);
    }

    @DisplayName("Should try to show a pet that is not registered in the database")
    @Test
    void getPet_ExceptionNotFound() {
        final String ID = "123456";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.getPet(ID);
        });
    }

    @DisplayName("Must update a specific pet by his id")
    @Test
    void updatePet() {
        Mockito.when(repository.findById("123456")).thenReturn(Optional.ofNullable(this.petEntity));

        service.updatePet("123456", this.requestPetDto);
        Mockito.verify(repository).save(this.petEntity);
    }

    @DisplayName("Should try to update a pet that is not registered in the database")
    @Test
    void updatePet_ExceptionNotFound() {
        final String ID = "123456";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.updatePet(ID, this.requestPetDto);
        });
    }

    @DisplayName("Must delete a pet by the id that has not yet been adopted")
    @Test
    void deletePet() {
        final String ID = "123456";

        List<ResponseAdoptionDto> adoptionList = new ArrayList<>();
        ResponseAdoptionDto adop = new ResponseAdoptionDto();
        ResponseAdoptionPetDto pet = new ResponseAdoptionPetDto();
        adop.setPet(pet);
        adoptionList.add(adop);
        Mockito.when(adoption.getAdoption()).thenReturn(adoptionList);
        adoptionList.get(0).getPet().setId("0");

        Mockito.when(repository.findById(ID)).thenReturn(Optional.ofNullable(this.petEntity));

        service.deletePet(ID);
        Mockito.verify(repository).delete(this.petEntity);
    }

    @DisplayName("Should try to delete a pet by the id that was already adopted")
    @Test
    void deletePet_ExceptionPetCannotBeDeleted() {
        final String ID = "123456";

        List<ResponseAdoptionDto> adoptionList = new ArrayList<>();
        ResponseAdoptionDto adop = new ResponseAdoptionDto();
        ResponseAdoptionPetDto pet = new ResponseAdoptionPetDto();
        adop.setPet(pet);
        adoptionList.add(adop);
        Mockito.when(adoption.getAdoption()).thenReturn(adoptionList);
        adoptionList.get(0).getPet().setId(ID);

        Mockito.when(repository.findById(ID)).thenReturn(Optional.ofNullable(this.petEntity));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.deletePet(ID);
        });
    }

    @DisplayName("Should try to delete a pet that is not registered in the database")
    @Test
    void deletePet_ExceptionNotFound() {
        final String ID = "123456";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.deletePet(ID);
        });
    }

    @DisplayName("Verify if pet was already adopted")
    @Test
    void verifyIfPetHasBeenAdopted() {
        final String ID = "123456";
        List<ResponseAdoptionDto> adoptionList = new ArrayList<>();
        ResponseAdoptionDto adop = new ResponseAdoptionDto();
        ResponseAdoptionPetDto pet = new ResponseAdoptionPetDto();
        adop.setPet(pet);
        adoptionList.add(adop);
        Mockito.when(adoption.getAdoption()).thenReturn(adoptionList);
        adoptionList.get(0).getPet().setId("0");

        Assertions.assertFalse(service.verifyIfPetHasBeenAdopted(ID));

        adoptionList.get(0).getPet().setId(ID);
        Assertions.assertTrue(service.verifyIfPetHasBeenAdopted(ID));
    }

    @DisplayName("Verify if ong is getted by cnpj")
    @Test
    void getByOngId() {
        final String CNPJ = "123456";

        ResponseOngDto ongDto = new ResponseOngDto();
        ongDto.setCnpj(CNPJ);
        Mockito.when(ong.getOng(CNPJ)).thenReturn(ongDto);

        PetEntity pet = new PetEntity();
        pet.setOngId(CNPJ);
        List<PetEntity> petEntity = new ArrayList<>();
        petEntity.add(pet);

        Mockito.when(repository.findByOngId(CNPJ)).thenReturn(petEntity);

        List<ResponsePetDto> res = service.getByOngId(CNPJ);

        Assertions.assertTrue(res.size() == 1);
        Assertions.assertTrue(res.get(0).getOng().getCnpj().equals(CNPJ));
    }

    @DisplayName("Verify if ong is getted by cnpj")
    @Test
    void getByOngId_Empty() {
        final String CNPJ = "123456";
        List<PetEntity> petEntity = new ArrayList<>();
        Mockito.when(repository.findByOngId(CNPJ)).thenReturn(petEntity);

        List<ResponsePetDto> res = service.getByOngId(CNPJ);

        Assertions.assertTrue(res.size() == 0);
    }

}

