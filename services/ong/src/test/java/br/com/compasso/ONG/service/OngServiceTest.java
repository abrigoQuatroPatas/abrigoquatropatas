package br.com.compasso.ONG.service;

import br.com.compasso.ONG.dto.request.RequestAddressDto;
import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.dto.request.RequestOngPutDto;
import br.com.compasso.ONG.dto.response.*;
import br.com.compasso.ONG.entity.OngEntity;
import br.com.compasso.ONG.http.PetClient;
import br.com.compasso.ONG.http.VoluntaryClient;
import br.com.compasso.ONG.http.ZipCodeClient;
import br.com.compasso.ONG.repository.OngRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

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

    private RequestOngPutDto ongPutDto;

    @Mock
    private PetClient petClient;

    @Mock
    private VoluntaryClient voluntaryClient;

    @Mock
    private ZipCodeClient zipCodeClient;

    @Mock
    private WebTestClient webClient;

    private Mono<ZipCodeResponse> zipCodeResponse;

    @BeforeEach
    public void setUp() {
        this.ongEntity = new OngEntity();
        this.ongEntity.setCnpj("45984180000121");

        ZipCodeResponse zipCode = new ZipCodeResponse();
        zipCode.setState("SC");
        zipCode.setCity("Florianópolis");
        zipCode.setDistrict("Armação do Pântano do Sul");
        zipCode.setStreet("Rodovia Francisco Thomaz dos Santos");

        this.zipCodeResponse = Mono.just(zipCode);

        RequestAddressDto addressDto = RequestAddressDto.builder()
                .zipCode("88066-260")
                .state("SC")
                .city("Florianópolis")
                .district("Armação do Pântano do Sul")
                .street("Rodovia Francisco Thomaz dos Santos")
                .number("4400")
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

    @DisplayName("Must register an ong")
    @Test
    void post() {
        Mockito.when(modelMapper.map(this.ongDto, OngEntity.class)).thenReturn(this.ongEntity);
        Mockito.when(repository.existsById(ongEntity.getCnpj())).thenReturn(false);
        Mockito.when(zipCodeClient.findAddressByOng(Mockito.any())).thenReturn(zipCodeResponse);
        service.post(this.ongDto);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @DisplayName("Must update an ong by cnpj")
    @Test
    void update() {
        Mockito.when(repository.findById("45984180000121")).thenReturn(Optional.ofNullable(this.ongEntity));

        service.update("45984180000121", this.ongPutDto);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @DisplayName("Should try to update a ong that is not registered in the database")
    @Test
    void updateException() {
        Mockito.when(repository.findById("45984180000121")).thenReturn(Optional.ofNullable(this.ongEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.update("53934042040", this.ongPutDto));
    }

    @DisplayName("Must show all ongs")
    @Test
    void getAll_Success() {
        PageRequest pagination = PageRequest.of(1, 10);

        List<OngEntity> ong = Arrays.asList(new OngEntity());
        Page<OngEntity> ongPage = new PageImpl<>(ong, pagination, ong.size());

        List<ResponseOngDto> ordersDto = Arrays.asList(new ResponseOngDto());
        Page<ResponseOngDto> pageDto = new PageImpl<>(ordersDto, pagination, ordersDto.size());

        Mockito.when(repository.findAll(pagination)).thenReturn(ongPage);

        Page<ResponseOngDto> res = service.getAll(pagination);
        Assertions.assertTrue(res.getContent().size() == 1);
    }

    @DisplayName("Must show all pets")
    @Test
    void getWithPets_Success() {
        final String CNPJ = "45984180000121";
        List<ResponsePetDto> petList = new ArrayList<>();
        ResponsePetDto pet = new ResponsePetDto();
        petList.add(pet);
        ResponseOngPetsDto responseOngPetsDto = new ResponseOngPetsDto();

        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(petClient.getOngId(CNPJ)).thenReturn(petList);
        Mockito.when(
                modelMapper.map(ongEntity, ResponseOngPetsDto.class)
        ).thenReturn(responseOngPetsDto);

        ResponseOngPetsDto resp = service.getWithPets(CNPJ);
        Assertions.assertTrue(resp.getPetIds().size() == 1);
    }

    @DisplayName("Must try show all pets from a ong with id not registered in the database")
    @Test
    void getWithPets_ExceptionCnpjNotFound()  {
        final String CNPJ = "45984180000121";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.getWithPets(CNPJ);
        });
    }

    @DisplayName("Must show all voluntaries")
    @Test
    void getWithVoluntaries_Success() {
        final String CNPJ = "45984180000121";
        List<ResponseVoluntaryDto> voluntaryList = new ArrayList<>();
        ResponseVoluntaryDto voluntary = new ResponseVoluntaryDto();
        voluntaryList.add(voluntary);
        ResponseOngVolunteersDto ongVolunteersDto = new ResponseOngVolunteersDto();

        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(voluntaryClient.getOngId(CNPJ)).thenReturn(voluntaryList);
        Mockito.when(
                modelMapper.map(ongEntity, ResponseOngVolunteersDto.class)
        ).thenReturn(ongVolunteersDto);

        ResponseOngVolunteersDto resp = service.getWithVoluntaries(CNPJ);
        Assertions.assertTrue(resp.getVoluntaries().size() == 1);
    }

    @DisplayName("Must try show all voluntaries from a ong with id not registered in the database")
    @Test
    void getWithVoluntaries_ExceptionCnpjNotFound() {
        final String CNPJ = "45984180000121";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.getWithVoluntaries(CNPJ);
        });
    }

    @DisplayName("Must show a specific ong by cnpj")
    @Test
    void getWithoutVoluntaries_Success() {
        final String CNPJ = "45984180000121";
        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));

        ResponseOngDto resp = service.getWithoutVoluntaries(CNPJ);
    }

    @DisplayName("Should try to show an ong that is not registered in the database")
    @Test
    void getWithoutVoluntaries_ExceptionCnpjNotFound() {
        final String CNPJ = "45984180000121";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.getWithoutVoluntaries(CNPJ);
        });
    }

    @DisplayName("Must put amount of cats in ong")
    @Test
    void putAmountCat_Success() {
        final String CNPJ = "45984180000121";
        this.ongEntity.setAmountCat(2);
        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(repository.save(ongEntity)).thenReturn(this.ongEntity);

        service.putAmount(CNPJ, "cat");
        Assertions.assertTrue(this.ongEntity.getAmountCat() == 3);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @DisplayName("Must put amount of dogs in ong")
    @Test
    void putAmountDog_Success() {
        final String CNPJ = "45984180000121";
        this.ongEntity.setAmountDog(2);
        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(repository.save(ongEntity)).thenReturn(this.ongEntity);

        service.putAmount(CNPJ, "dog");
        Assertions.assertTrue(this.ongEntity.getAmountDog() == 3);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @DisplayName("Should try to put amount of pet type from an ong that is not registered in the database")
    @Test
    void putAmount_ExceptionCnpjNotFound() {
        final String CNPJ = "45984180000121";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.putAmount(CNPJ, "dog");
        });
    }

    @DisplayName("Must delete amount of cats in ong")
    @Test
    void deleteAmountCat_Success() {
        final String CNPJ = "45984180000121";
        this.ongEntity.setAmountCat(2);
        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(repository.save(ongEntity)).thenReturn(this.ongEntity);

        service.deleteAmount(CNPJ, "cat");
        Assertions.assertTrue(this.ongEntity.getAmountCat() == 1);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @DisplayName("Must delete amount of dogs in ong")
    @Test
    void deleteAmountDog_Success() {
        final String CNPJ = "45984180000121";
        this.ongEntity.setAmountDog(2);
        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(repository.save(ongEntity)).thenReturn(this.ongEntity);

        service.deleteAmount(CNPJ, "dog");
        Assertions.assertTrue(this.ongEntity.getAmountDog() == 1);
        Mockito.verify(repository).save(this.ongEntity);
    }

    @DisplayName("Should try delete amount of pet type from an ong that is not registered in the database")
    @Test
    void deleteAmount_ExceptionCnpjNotFound() {
        final String CNPJ = "45984180000121";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.deleteAmount(CNPJ, "dog");
        });
    }

    @DisplayName("Must delete a voluntary")
    @Test
    void deleteVoluntary_Success() {
        final String CNPJ = "45984180000121";
        final String CPF = "45628054312";
        List<String> voluntaryList = new ArrayList<>();;
        voluntaryList.add(CPF);
        this.ongEntity.setVoluntaryIds(voluntaryList);

        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));

        service.deleteVoluntary(CNPJ, CPF);

        Assertions.assertTrue(this.ongEntity.getVoluntaryIds().isEmpty());
    }

    @DisplayName("Should try to delete a voluntary from an ong that is not registered in the database")
    @Test
    void deleteVoluntary_ExceptionCnpjNotFound() {
        final String CNPJ = "45984180000121";
        final String CPF = "45628054312";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.deleteVoluntary(CNPJ, CPF);
        });
    }

    @DisplayName("Must update a voluntary")
    @Test
    void updateVoluntary_Success() {
        final String CNPJ = "45984180000121";
        final String CPF = "45628054312";
        List<String> voluntaryList = new ArrayList<>();
        this.ongEntity.setVoluntaryIds(voluntaryList);

        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));

        service.updateVoluntary(CNPJ, CPF);
        Mockito.verify(repository).save(this.ongEntity);
        Assertions.assertTrue(this.ongEntity.getVoluntaryIds().size() == 1);
    }

    @DisplayName("Should try to update a voluntary from an ong that is not registered in the database")
    @Test
    void updateVoluntary_ExceptionCnpjNotFound() {
        final String CNPJ = "45984180000121";
        final String CPF = "45628054312";
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.updateVoluntary(CNPJ, CPF);
        });
    }

    @DisplayName("Must delete an ong")
    @Test
    void delete_WithSucess() {
        final String CNPJ = "45984180000121";
        List<ResponsePetDto> petList = new ArrayList<>();
        List<ResponseVoluntaryDto> voluntaryList = new ArrayList<>();

        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(petClient.getOngId(CNPJ)).thenReturn(petList);
        Mockito.when(voluntaryClient.getOngId(CNPJ)).thenReturn(voluntaryList);

        service.delete(CNPJ);
        Mockito.verify(repository).delete(this.ongEntity);
    }

    @DisplayName("Should try delete an ong with pets and/or voluntaries without success")
    @Test
    void delete_FailedBecauseNotEmpty() {
        final String CNPJ = "45984180000121";
        List<ResponsePetDto> petList = new ArrayList<>();
        List<ResponseVoluntaryDto> voluntaryList = new ArrayList<>();
        ResponsePetDto pet = new ResponsePetDto();
        ResponseVoluntaryDto voluntary = new ResponseVoluntaryDto();
        petList.add(pet);
        voluntaryList.add(voluntary);

        Mockito.when(repository.findById(CNPJ)).thenReturn(Optional.ofNullable(this.ongEntity));
        Mockito.when(petClient.getOngId(CNPJ)).thenReturn(petList);
        Mockito.when(voluntaryClient.getOngId(CNPJ)).thenReturn(voluntaryList);

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            service.delete(CNPJ);
        });
    }

    @DisplayName("Should try to delete an ong that is not registered in the database")
    @Test
    void deleteException() {
        Mockito.when(repository.findById("45984180000121")).thenReturn(Optional.ofNullable(this.ongEntity));
        Assertions.assertThrows(ResponseStatusException.class,() -> service.delete("53934042040"));
    }
}