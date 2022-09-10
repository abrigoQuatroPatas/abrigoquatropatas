package br.com.compasso.client.controller;

import br.com.compasso.client.dto.request.RequestClientDto;
import br.com.compasso.client.dto.request.RequestClientPutDto;
import br.com.compasso.client.dto.response.ResponseClientDto;
import br.com.compasso.client.handler.MensagemErro;
import br.com.compasso.client.service.ClientService;
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
@RequestMapping("/client")
@Tag(name = "Client", description = "The client API")
public class ClientController {

    @Autowired
    private ClientService service;

    @Operation(summary = "Add a new client to the database", description = "Add a new client to the database ", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseClientDto.class))),
            @ApiResponse(responseCode = "405", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PostMapping()
    public ResponseEntity<ResponseClientDto> post(@RequestBody @Valid RequestClientDto client, UriComponentsBuilder componentsBuilder) {
        ResponseClientDto clientDto = service.post(client);
        URI uri = componentsBuilder.path("/client/{cpf}").buildAndExpand(clientDto.getCpf()).toUri();
        return ResponseEntity.created(uri).body(clientDto);
    }

    @Operation(summary = "Get all clients", description = "Get all clients ", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Page<ResponseClientDto>> getAll(Pageable pageable) {
        Page<ResponseClientDto> all = service.getAll(pageable);
        return ResponseEntity.ok(all);
    }

    @Operation(summary = "Get a client", description = "Get a client by cpf ", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @GetMapping(value = "/{cpf}", produces = "application/json")
    public ResponseEntity<ResponseClientDto> get(@PathVariable String cpf) {
        ResponseClientDto clientDto = service.get(cpf);
        return ResponseEntity.ok(clientDto);
    }

    @Operation(summary = "Updates the status client", description = "Update a client's status ", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PutMapping("/{cpf}")
    public ResponseEntity<Void> put(@PathVariable String cpf, @RequestBody @Valid RequestClientPutDto client) {
        service.update(cpf, client);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cpf}/inProgress")
    public ResponseEntity<Void> putStatusInProgress(@PathVariable String cpf) {
        service.putStatusInProgress(cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the status client", description = "Update a client's status to approved ", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PutMapping("/{cpf}/approved")
    public ResponseEntity<Void> putStatusApproved(@PathVariable String cpf) {
        service.putStatusApproved(cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the status client", description = "Update a client's status to disapproved ", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PutMapping("/{cpf}/disapproved")
    public ResponseEntity<Void> putStatusDisapproved(@PathVariable String cpf) {
        service.putStatusDisapproved(cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Updates the status client", description = "Update a client's status to null ", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @PutMapping("/{cpf}/null")
    public ResponseEntity<Void> putStatusNull(@PathVariable String cpf) {
        service.putStatusNull(cpf);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete a client", description = "Delete a client by cpf", tags = {"Client"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content operation"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Validation exception",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MensagemErro.class))
            )
    })
    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}