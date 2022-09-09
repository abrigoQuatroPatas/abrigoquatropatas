package br.com.compasso.ONG.controller;

import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.dto.request.RequestOngPutDto;
import br.com.compasso.ONG.dto.response.ResponseOngDto;
import br.com.compasso.ONG.dto.response.ResponseOngPetsDto;
import br.com.compasso.ONG.dto.response.ResponseOngVolunteersDto;
import br.com.compasso.ONG.handler.MensagemErro;
import br.com.compasso.ONG.service.OngService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/ong")
@Tag(name = "Ong", description = "The ong API")
public class OngController {

    @Autowired
    private OngService service;

    @Operation(summary = "Add a new ong to the database", description = "Add a new ong to the database", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Número do registro de contribuinte corporativo brasileiro (CNPJ) inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))),
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseOngDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid zipCode!",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PostMapping
    public ResponseEntity<ResponseOngDto> post(@RequestBody @Valid RequestOngDto ong, UriComponentsBuilder component) {
        ResponseOngDto ongCadastrada = service.post(ong);
        URI uri = component.path("/ong/{cnpj}").buildAndExpand(ongCadastrada.getCnpj()).toUri();
        return ResponseEntity.created(uri).body(ongCadastrada);
    }

    @Operation(summary = "Get all ongs", description = "Get a list of all ongs", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<ResponseOngDto>> getAll(Pageable pageable) {
        Page<ResponseOngDto> ongs = service.getAll(pageable);
        return ResponseEntity.ok(ongs);
    }

    @Operation(summary = "Get a voluntary list", description = "Get a voluntary list by ong cnpj", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping("/{cnpj}/voluntaries")
    public ResponseEntity<ResponseOngVolunteersDto> getWithVoluntaries(@PathVariable String cnpj) {
        ResponseOngVolunteersDto ong = service.getWithVoluntaries(cnpj);
        return ResponseEntity.ok(ong);
    }

    @Operation(summary = "Get a pet list", description = "Get a pet list by ong cnpj", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping("/{cnpj}/pets")
    public ResponseEntity<ResponseOngPetsDto> getWithPets(@PathVariable String cnpj) {
        ResponseOngPetsDto ong = service.getWithPets(cnpj);
        return ResponseEntity.ok(ong);
    }

    @Operation(summary = "Get a ong", description = "Get a ong by cnpj", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content),

    })
    @GetMapping("/{cnpj}")
    public ResponseEntity<ResponseOngDto> getWithoutVoluntaries(@PathVariable String cnpj) {
        ResponseOngDto ong = service.getWithoutVoluntaries(cnpj);
        return ResponseEntity.ok(ong);
    }

    @Operation(summary = "Update a ong", description = "Update a ong by cnpj", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content)
    })
    @PutMapping("/{cnpj}")
    public ResponseEntity<Void> put(@PathVariable String cnpj, @RequestBody RequestOngPutDto ong) {
        service.update(cnpj, ong);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the ong amount", description = "Update a ong's pet amont by pet type and ong cnpj", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PutMapping("/{cnpj}/amount/{type}")
    public ResponseEntity<Void> putAmount(@PathVariable String cnpj, @PathVariable String type) {
        service.putAmount(cnpj, type);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a ong", description = "Delete a ong by cnpj", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> delete(@PathVariable String cnpj) {
        service.delete(cnpj);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a voluntary", description = "Delete a voluntary witch his cpf from ong using her id", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content)
    })
    @DeleteMapping("/{cnpj}/voluntary/{cpf}")
    public ResponseEntity<Void> deleteVoluntary(@PathVariable String cnpj, @PathVariable String cpf) {
        service.deleteVoluntary(cnpj, cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update a voluntary", description = "Update a voluntary witch his cpf from ong using her id", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content)
    })
    @PutMapping("/{cnpj}/voluntary/{cpf}")
    public ResponseEntity<Void> putVoluntary(@PathVariable String cnpj, @PathVariable String cpf) {
        service.updateVoluntary(cnpj, cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a pet type amount", description = "Delete a pet type amount from ong by cnpj", tags = {"Ong"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content)
    })
    @DeleteMapping("/{cnpj}/amount/{type}")
    public ResponseEntity<Void> deleteAmount(@PathVariable String cnpj, @PathVariable String type) {
        service.deleteAmount(cnpj, type);
        return ResponseEntity.noContent().build();
    }
}
