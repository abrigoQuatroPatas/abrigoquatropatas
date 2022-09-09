package br.com.compasso.voluntary.controller;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.request.RequestVoluntaryPutDto;
import br.com.compasso.voluntary.dto.response.ResponseVoluntaryDto;
import br.com.compasso.voluntary.exception.ErrorMessage;
import br.com.compasso.voluntary.service.VoluntaryService;
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
import java.util.List;

@RestController
@RequestMapping("/voluntary")
@Tag(name = "Voluntary", description = "Voluntary API")
public class VoluntaryController {

    @Autowired
    private VoluntaryService service;

    @Operation(summary = "Get all voluntaries", description = "Get a list of all voluntaries", tags = {"Voluntary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @GetMapping
    public ResponseEntity<Page<ResponseVoluntaryDto>> getAll(Pageable pageable) {
        Page<ResponseVoluntaryDto> voluntaries = service.getAll(pageable);
        return ResponseEntity.ok(voluntaries);
    }

    @Operation(summary = "Get a voluntary", description = "Get voluntary by id", tags = {"Voluntary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Voluntary not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @GetMapping("/{cpf}")
    public ResponseEntity<ResponseVoluntaryDto> get(@PathVariable String cpf) {
        ResponseVoluntaryDto voluntary = service.get(cpf);
        return ResponseEntity.ok(voluntary);
    }

    @Operation(summary = "Add a new voluntary to the database", description = "Add a new voluntary to the database", tags = {"Voluntary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseVoluntaryDto.class))),
            @ApiResponse(responseCode = "405", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @PostMapping("/ong/{ongId}")
    public ResponseEntity<ResponseVoluntaryDto> post(@RequestBody @Valid RequestVoluntaryDto voluntary, UriComponentsBuilder componentsBuilder, @PathVariable String ongId) {
        ResponseVoluntaryDto voluntaryDto = service.post(voluntary,ongId);
        URI uri = componentsBuilder.path("/voluntary/{cpf}/ong/{ongId}").buildAndExpand(voluntaryDto.getCpf(), ongId).toUri();
        return ResponseEntity.created(uri).body(voluntaryDto);
    }

    @Operation(summary = "Update voluntary", description = "Vinculate a voluntary to ong ", tags = {"Voluntary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Voluntary/Ong not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @PutMapping("/{cpf}")
    public ResponseEntity<Void> put(@PathVariable String cpf, @RequestBody @Valid RequestVoluntaryPutDto voluntary) {
        service.update(cpf, voluntary);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a voluntary", description = "Delete voluntary by id", tags = {"Voluntary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Voluntary not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get a cnpj", description = "Get ong by id", tags = {"Voluntary"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Ong not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    @GetMapping("/ong/{cnpj}")
    public ResponseEntity<List<ResponseVoluntaryDto>> getOngId(@PathVariable String cnpj) {
        List<ResponseVoluntaryDto> voluntary = service.getByOngId(cnpj);
        return ResponseEntity.ok(voluntary);
    }

}
