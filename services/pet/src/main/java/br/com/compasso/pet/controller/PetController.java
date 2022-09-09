package br.com.compasso.pet.controller;

import br.com.compasso.pet.dto.request.RequestPetDto;
import br.com.compasso.pet.dto.response.ResponsePetDto;
import br.com.compasso.pet.handler.MensagemErro;
import br.com.compasso.pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pet")
@Tag(name = "Pet", description = "The pet API")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "Add a new pet to the database", description = "Add a new pet to the database", tags = {"Pet"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponsePetDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid zipCode!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PostMapping("/ong/{cnpj}")
    public ResponseEntity<ResponsePetDto> postPet(@RequestBody @Valid RequestPetDto pet, UriComponentsBuilder component, @PathVariable String cnpj) {
        log.info("postPet() - START - Calling the service");
        ResponsePetDto petRegistered = petService.postPet(pet, cnpj);
        URI uri = component.path("/pet/{id}/ong/{cnpj}").buildAndExpand(petRegistered.getId(), cnpj).toUri();
        return ResponseEntity.created(uri).body(petRegistered);
    }

    @Operation(summary = "Get all pets", description = "Get a list of all pets", tags = {"Pet"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<ResponsePetDto>> getAllPets(Pageable pageable) {
        log.info("getAllPets() - START - Calling the service");
        Page<ResponsePetDto> pets = petService.getAllPets(pageable);
        return ResponseEntity.ok(pets);
    }

    @Operation(summary = "Get a pet", description = "Get a pet by id", tags = {"Pet"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content),

    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponsePetDto> getPet(@PathVariable String id) {
        log.info("getPet() - START - Calling the service");
        ResponsePetDto pet = petService.getPet(id);
        return ResponseEntity.ok(pet);
    }

    @Operation(summary = "Get a ong", description = "Get a ong by id", tags = {"Pet"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content),

    })
    @GetMapping("/ong/{cnpj}")
    public ResponseEntity<List<ResponsePetDto>> getOngId(@PathVariable String cnpj) {
        List<ResponsePetDto> pet = petService.getByOngId(cnpj);
            return ResponseEntity.ok(pet);
        }

    @Operation(summary = "Update a pet", description = "Update a pet ", tags = {"Pet"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> putPet(@PathVariable String id, @RequestBody @Valid RequestPetDto pet) {
        log.info("putPet() - START - Calling the service");
        petService.updatePet(id, pet);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a pet", description = "Delete a pet by id", tags = {"Pet"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable String id) {
        log.info("deletePet() - START - Calling the service");
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
