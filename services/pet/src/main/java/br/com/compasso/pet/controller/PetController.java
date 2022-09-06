package br.com.compasso.pet.controller;

import br.com.compasso.pet.dto.request.PetRequestDto;
import br.com.compasso.pet.dto.response.PetResponseDto;
import br.com.compasso.pet.service.PetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
        private PetService petService;

        @PostMapping("/ong/{cnpj}")
        public ResponseEntity<PetResponseDto> postPet(@RequestBody @Valid PetRequestDto pet, UriComponentsBuilder component, @PathVariable String cnpj) {
            log.info("postPet() - START - Calling the service");
            PetResponseDto petRegistered = petService.postPet(pet, cnpj);
            URI uri = component.path("/pet/{id}/ong/{cnpj}").buildAndExpand(petRegistered.getId(), cnpj).toUri();
            return ResponseEntity.created(uri).body(petRegistered);
        }

        @GetMapping
        public ResponseEntity<Page<PetResponseDto>> getAllPets(Pageable pageable) {
            log.info("getAllPets() - START - Calling the service");
            Page<PetResponseDto> pets = petService.getAllPets(pageable);
            return ResponseEntity.ok(pets);
        }

        @GetMapping("/{id}")
        public ResponseEntity<PetResponseDto> getPet(@PathVariable String id) {
            log.info("getPet() - START - Calling the service");
            PetResponseDto pet = petService.getPet(id);
            return ResponseEntity.ok(pet);
        }

        @PutMapping("/{id}")
        public ResponseEntity<Void> putPet(@PathVariable String id, @RequestBody @Valid PetRequestDto pet) {
            log.info("putPet() - START - Calling the service");
            petService.updatePet(id, pet);
            return ResponseEntity.noContent().build();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePet(@PathVariable String id) {
            log.info("deletePet() - START - Calling the service");
            petService.deletePet(id);
            return ResponseEntity.noContent().build();
        }
}
