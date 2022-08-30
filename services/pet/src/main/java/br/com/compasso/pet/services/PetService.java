package br.com.compasso.pet.services;

import br.com.compasso.pet.dtos.request.PetRequestDto;
import br.com.compasso.pet.dtos.response.PetResponseDto;
import br.com.compasso.pet.entities.PetEntity;
import br.com.compasso.pet.repositories.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PetResponseDto postPet(PetRequestDto petRequestDto) {
        log.info("postPet() - START - Saving pet");

        PetEntity petEntity = modelMapper.map(petRequestDto, PetEntity.class);
        PetEntity save = petRepository.save(petEntity);

        log.info("postPet() - END - Pet saved");

        return modelMapper.map(save, PetResponseDto.class);
    }

    public Page<PetResponseDto> getAllPets(Pageable pageable) {
        log.info("getAllPets() - START - Getting all pets");

        Page<PetEntity> all = petRepository.findAll(pageable);

        log.info("getAllPets() - END - all pets obtained");

        return all.map(pet -> modelMapper.map(pet, PetResponseDto.class));
    }

    public PetResponseDto getPet(String id) {
        log.info("getPet() - START - Getting pet");

        PetEntity petEntity = petRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        log.info("getPet() - END - Pet obtained");

        return modelMapper.map(petEntity, PetResponseDto.class);
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
        petRepository.delete(petEntity);

        log.info("deletePet() - END - Pet deleted");
    }
}