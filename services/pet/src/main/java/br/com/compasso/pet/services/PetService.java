package br.com.compasso.pet.services;

import br.com.compasso.pet.dtos.request.PetRequestDto;
import br.com.compasso.pet.dtos.response.PetResponseDto;
import br.com.compasso.pet.dtos.response.RedemptionAddressResponseDto;
import br.com.compasso.pet.dtos.response.ResponseOngDto;
import br.com.compasso.pet.entities.PetEntity;
import br.com.compasso.pet.exceptions.MessageFeignException;
import br.com.compasso.pet.https.OngClient;
import br.com.compasso.pet.repositories.PetRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OngClient ong;


    public PetResponseDto postPet(PetRequestDto petRequestDto, String cnpj) {
        log.info("postPet() - START - Saving pet");

        ResponseOngDto ongDto;
        try {
            ongDto = ong.getOng(cnpj);
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }
        PetEntity petEntity = modelMapper.map(petRequestDto, PetEntity.class);
        petEntity.setOngId(cnpj);
        String type = String.valueOf(petEntity.getType()).toLowerCase();
        ong.putAmount(cnpj, type);
        PetEntity save = petRepository.save(petEntity);
        log.info("postPet() - END - Pet saved");
        RedemptionAddressResponseDto address = modelMapper.map(petEntity.getRedemptionAddress(), RedemptionAddressResponseDto.class);
        return PetResponseDto.builder()
                .id(petEntity.getId())
                .name(petEntity.getName())
                .arrivalDate(petEntity.getArrivalDate())
                .redemptionAddress(address)
                .type(petEntity.getType())
                .ong(ongDto)
                .build();
    }

    public Page<PetResponseDto> getAllPets(Pageable pageable) {
        log.info("getAllPets() - START - Getting all pets");

        Page<PetEntity> all = petRepository.findAll(pageable);
        List<PetResponseDto> petResponseDtoList = new ArrayList<>();

        all.forEach(petEntity -> {
            RedemptionAddressResponseDto address = modelMapper.map(petEntity.getRedemptionAddress(), RedemptionAddressResponseDto.class);
            ResponseOngDto ongDto = ong.getOng(petEntity.getOngId());
            PetResponseDto petDto = PetResponseDto.builder()
                    .id(petEntity.getId())
                    .type(petEntity.getType())
                    .redemptionAddress(address)
                    .name(petEntity.getName())
                    .arrivalDate(petEntity.getArrivalDate())
                    .ong(ongDto)
                    .build();
            petResponseDtoList.add(petDto);
        });

        log.info("getAllPets() - END - all pets obtained");

        return new PageImpl<>(petResponseDtoList);
    }

    public PetResponseDto getPet(String id) {
        log.info("getPet() - START - Getting pet");

        PetEntity petEntity = petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ResponseOngDto ongDto;
        try {
            ongDto = ong.getOng(petEntity.getOngId());
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }

        RedemptionAddressResponseDto address = modelMapper.map(petEntity.getRedemptionAddress(), RedemptionAddressResponseDto.class);
        return PetResponseDto.builder()
                .id(petEntity.getId())
                .name(petEntity.getName())
                .arrivalDate(petEntity.getArrivalDate())
                .redemptionAddress(address)
                .type(petEntity.getType())
                .ong(ongDto)
                .build();

    }

    public void updatePet(String id, PetRequestDto pet) {
        log.info("updatePet() - START - Updating pet");

        PetEntity petEntity = petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(pet, petEntity);
        petRepository.save(petEntity);

        log.info("deletePet() - END - Pet updated");
    }

    public void deletePet(String id) {
        log.info("deletePet() - START - Deleting pet");

        PetEntity petEntity = petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ong.deleteAmount(petEntity.getOngId(), String.valueOf(petEntity.getType()).toLowerCase());
        petRepository.delete(petEntity);
        log.info("deletePet() - END - Pet deleted");
    }

}