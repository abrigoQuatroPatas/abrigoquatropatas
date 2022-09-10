package br.com.compasso.adoption.controller;

import br.com.compasso.adoption.dto.response.ResponseAdoptionDto;
import br.com.compasso.adoption.handler.MensagemErro;
import br.com.compasso.adoption.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/adoption")
@Tag(name = "Adoption", description = "The adoption API")
public class AdoptionController {

    @Autowired
    private AdoptionService service;

    @Operation(summary = "Add a new adoption to the database", description = "Add a new adoption to the database", tags = {"Adoption"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseAdoptionDto.class))),
            @ApiResponse(responseCode = "405", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PostMapping("/consumer/{consumerId}/pet/{petId}")
    public ResponseEntity<ResponseAdoptionDto> post(@PathVariable String consumerId, @PathVariable String petId,
                                                    UriComponentsBuilder componentsBuilder) {
        ResponseAdoptionDto adoptionDto = service.post(consumerId, petId);

        URI uri = componentsBuilder.path("/adoption/{id}/consumer/{consumerId}/pet/{petId}")
                .buildAndExpand(adoptionDto.getId(), adoptionDto.getConsumer().getCpf(), adoptionDto.getPet().getId()).toUri();

        return ResponseEntity.created(uri).body(adoptionDto);
    }

    @Operation(summary = "Get all adoptions", description = "Get a list of all pets", tags = {"Adoption"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ResponseAdoptionDto>> get() {
        List<ResponseAdoptionDto> adoptions = service.get();
        return ResponseEntity.ok(adoptions);
    }

    @Operation(summary = "Get a adoption", description = "Get a adoption by id", tags = {"Adoption"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Adoption not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ResponseAdoptionDto> get(@PathVariable Long id) {
        ResponseAdoptionDto adoption = service.get(id);
        return ResponseEntity.ok(adoption);
    }

    @Operation(summary = "Delete a adoption", description = "Delete a adoption by id", tags = {"Adoption"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Adoption not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the status consumer", description = "Update a consumer's status to approved ", tags = {"Adoption"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "consumer not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PutMapping("/consumer/{idConsumer}/approved")
    public ResponseEntity<Void> putStatusApproved(@PathVariable String idConsumer) {
        service.putStatusConsumerApproved(idConsumer);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the status consumer", description = "Update a consumer's status to disapproved ", tags = {"Adoption"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "consumer not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PutMapping("/consumer/{idConsumer}/disapproved")
    public ResponseEntity<Void> putStatusDisapproved(@PathVariable String idConsumer) {
        service.putStatusConsumerDisapproved(idConsumer);
        return ResponseEntity.noContent().build();
    }

}
