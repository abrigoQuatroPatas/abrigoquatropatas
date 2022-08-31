package br.com.compasso.client.controllers;

import br.com.compasso.client.dtos.request.RequestClientDto;
import br.com.compasso.client.dtos.response.ResponseClientDto;
import br.com.compasso.client.services.ClientService;
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
public class ClientController {

    @Autowired
    private ClientService service;

    @PostMapping
    public ResponseEntity<ResponseClientDto> post(@RequestBody @Valid RequestClientDto client, UriComponentsBuilder componentsBuilder) {
        ResponseClientDto clientDto = service.post(client);
        URI uri = componentsBuilder.path("/client/{cpf}").buildAndExpand(clientDto.getCpf()).toUri();
        return ResponseEntity.created(uri).body(clientDto);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseClientDto>> getAll(Pageable pageable) {
        Page<ResponseClientDto> all = service.getAll(pageable);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ResponseClientDto> get(@PathVariable String cpf) {
        ResponseClientDto clientDto = service.get(cpf);
        return ResponseEntity.ok(clientDto);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Void> put(@PathVariable String cpf, @RequestBody @Valid RequestClientDto client) {
        service.update(cpf, client);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cpf}/inProgress")
    public ResponseEntity<Void> putStatusInProgress(@PathVariable String cpf) {
        service.putStatusInProgress(cpf);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cpf}/approved")
    public ResponseEntity<Void> putStatusDisapproved(@PathVariable String cpf) {
        service.putStatusApproved(cpf);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cpf}/disapproved")
    public ResponseEntity<Void> putStatusApproved(@PathVariable String cpf) {
        service.putStatusDisapproved(cpf);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cpf}/null")
    public ResponseEntity<Void> putStatusNull(@PathVariable String cpf) {
        service.putStatusNull(cpf);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }

}
