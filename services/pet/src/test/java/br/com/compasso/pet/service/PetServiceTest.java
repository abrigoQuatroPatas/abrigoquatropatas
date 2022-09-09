package br.com.compasso.pet.service;

import br.com.compasso.pet.dto.request.RequestPetDto;
import br.com.compasso.pet.dto.request.RequestRedemptionAddressDto;
import br.com.compasso.pet.entity.PetEntity;
import br.com.compasso.pet.enums.Type;
import br.com.compasso.pet.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class PetServiceTest {

    @InjectMocks
    private PetService petService;
    @Mock
    private PetRepository petRepository;
    @Mock
    private ModelMapper modelMapper;
    private PetEntity petEntity;
    private RequestPetDto requestPetDto;

    @BeforeEach
    public void initializePet() {
        this.petEntity = new PetEntity();
        this.petEntity.setId("123456");

        RequestRedemptionAddressDto redemptionAddress = RequestRedemptionAddressDto.builder()
                .zipCode("88066-260")
                .street("Rod. SC-406")
                .city("Fpolis")
                .state("SC")
                .number("12345")
                .district("Armação")
                .redemptionDate(LocalDateTime.now())
                .build();

        this.requestPetDto = RequestPetDto.builder()
                .id("123456")
                .name("Pet Teste")
                .arrivalDate(LocalDate.now())
                .type(Type.CAT)
                .redemptionAddress(redemptionAddress)
                .build();
    }

    @Test
    void postPet() {
        Mockito.when(modelMapper.map(this.requestPetDto, PetEntity.class)).thenReturn(this.petEntity);

        petService.postPet(this.requestPetDto);
        Mockito.verify(petRepository).save(this.petEntity);
    }

    @Test
    void getPet() {
        Mockito.when(petRepository.findById("123456")).thenReturn(Optional.ofNullable(this.petEntity));

        petService.getPet("123456");
        Mockito.verify(petRepository).findById(this.requestPetDto.getId());
    }

    @Test
    void updatePet() {
        Mockito.when(petRepository.findById("123456")).thenReturn(Optional.ofNullable(this.petEntity));

        petService.updatePet("123456", this.requestPetDto);
        Mockito.verify(petRepository).save(this.petEntity);
    }

    @Test
    void deletePet() {
        Mockito.when(petRepository.findById("123456")).thenReturn(Optional.ofNullable(this.petEntity));

        petService.deletePet("123456");
        Mockito.verify(petRepository).delete(this.petEntity);
    }
}
