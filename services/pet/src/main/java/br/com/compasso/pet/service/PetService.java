package br.com.compasso.pet.service;

import br.com.compasso.pet.dto.response.*;
import br.com.compasso.pet.entity.RedemptionAddressEntity;
import br.com.compasso.pet.http.AdoptionClient;
import br.com.compasso.pet.validation.Validations;
import br.com.compasso.pet.dto.request.RequestPetDto;
import br.com.compasso.pet.entity.PetEntity;
import br.com.compasso.pet.exception.MessageFeignException;
import br.com.compasso.pet.http.OngClient;
import br.com.compasso.pet.http.ZipCodeClient;
import br.com.compasso.pet.repository.PetRepository;

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
    private ZipCodeClient zipCodeClient;

    @Autowired
    private OngClient ong;

    @Autowired
    private AdoptionClient adoption;


    public ResponsePetDto postPet(RequestPetDto requestPetDto, String cnpj) {
        log.info("postPet() - START - Saving pet");

        ResponseOngDto ongDto;
        try {
            ongDto = ong.getOng(cnpj);
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }

        PetEntity petEntity = modelMapper.map(requestPetDto, PetEntity.class);

        String zipCode = requestPetDto.getRedemptionAddress().getZipCode()
                .replaceAll("\\D", "" );

        if (Validations.validateZipCode(zipCode)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Invalid zipCode!");
        }

        ZipCodeResponse zipCodeResponse = zipCodeClient.findRedemptionAddressByPet(zipCode).block();

        RedemptionAddressEntity redemptionAddress = RedemptionAddressEntity.builder()
                .state(zipCodeResponse.getState())
                .city(zipCodeResponse.getCity())
                .district(zipCodeResponse.getDistrict())
                .street(zipCodeResponse.getStreet())
                .number(requestPetDto.getRedemptionAddress().getNumber())
                .redemptionDate(requestPetDto.getRedemptionAddress().getRedemptionDate())
                .build();

        petEntity.setRedemptionAddress(redemptionAddress);
        petEntity.getRedemptionAddress().setZipCode(zipCode.replaceAll("\\D", ""));

        petEntity.setOngId(cnpj);
        String type = String.valueOf(petEntity.getType()).toLowerCase();
        ong.putAmount(cnpj, type);
        petRepository.save(petEntity);
        log.info("postPet() - END - Pet saved");
        ResponseRedemptionAddressDto address = modelMapper.map(petEntity.getRedemptionAddress(), ResponseRedemptionAddressDto.class);
        return ResponsePetDto.builder()
                .id(petEntity.getId())
                .name(petEntity.getName())
                .arrivalDate(petEntity.getArrivalDate())
                .redemptionAddress(address)
                .type(petEntity.getType())
                .ong(ongDto)
                .build();
    }

    public Page<ResponsePetDto> getAllPets(Pageable pageable) {
        log.info("getAllPets() - START - Getting all pets");

        Page<PetEntity> all = petRepository.findAll(pageable);
        List<ResponsePetDto> responsePetDtoList = new ArrayList<>();

        all.forEach(petEntity -> {
            ResponseRedemptionAddressDto address = modelMapper.map(petEntity.getRedemptionAddress(), ResponseRedemptionAddressDto.class);
            ResponseOngDto ongDto = ong.getOng(petEntity.getOngId());
            ResponsePetDto petDto = ResponsePetDto.builder()
                    .id(petEntity.getId())
                    .type(petEntity.getType())
                    .redemptionAddress(address)
                    .name(petEntity.getName())
                    .arrivalDate(petEntity.getArrivalDate())
                    .ong(ongDto)
                    .build();
            responsePetDtoList.add(petDto);
        });

        log.info("getAllPets() - END - all pets obtained");

        return new PageImpl<>(responsePetDtoList);
    }

    public ResponsePetDto getPet(String id) {
        log.info("getPet() - START - Getting pet");

        PetEntity petEntity = petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ResponseOngDto ongDto;
        try {
            ongDto = ong.getOng(petEntity.getOngId());
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }

        ResponseRedemptionAddressDto address = modelMapper.map(petEntity.getRedemptionAddress(), ResponseRedemptionAddressDto.class);
        return ResponsePetDto.builder()
                .id(petEntity.getId())
                .name(petEntity.getName())
                .arrivalDate(petEntity.getArrivalDate())
                .redemptionAddress(address)
                .type(petEntity.getType())
                .ong(ongDto)
                .build();

    }

    public void updatePet(String id, RequestPetDto pet) {
        log.info("updatePet() - START - Updating pet");

        PetEntity petEntity = petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(pet, petEntity);
        petRepository.save(petEntity);

        log.info("deletePet() - END - Pet updated");
    }

    private boolean verifyIfPetHasBeenAdopted(String petId) {
        try {
            List<ResponseAdoptionDto> adoptionList = adoption.getAdoption();
            for (ResponseAdoptionDto a: adoptionList) {
                String adoptedPetId = a.getPet().getId();
                if (petId.equals(adoptedPetId)) {
                    return true;
                }
            }
        } catch (FeignException e) {
            log.error("verifyIfPetHasBeenAdopted(): " + e.getMessage());
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }
        return false;
    }

    public void deletePet(String id) {
        log.info("deletePet() - START - Deleting pet");

        if (verifyIfPetHasBeenAdopted(id) == false) {
            PetEntity petEntity = petRepository.findById(id).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found."));
            ong.deleteAmount(petEntity.getOngId(), String.valueOf(petEntity.getType()).toLowerCase());
            petRepository.delete(petEntity);
            log.info("deletePet() - END - Pet deleted");
        } else {
            log.error("deletePet() - This pet was already adopted");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This pet was already adopted and cannot be deleted.");
        }
    }

}