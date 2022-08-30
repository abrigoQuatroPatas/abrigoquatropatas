package br.com.compasso.client.controllers;

<<<<<<< HEAD

import br.com.compasso.client.dtos.request.RequestClientDTO;
import br.com.compasso.client.dtos.response.ResponseClientDTO;
import br.com.compasso.client.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
=======
import br.com.compasso.client.dtos.request.RequestClientDto;
import br.com.compasso.client.dtos.response.ResponseClientDto;
import br.com.compasso.client.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
>>>>>>> origin/master
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
<<<<<<< HEAD
import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ResponseClientDTO> post(@RequestBody @Valid RequestClientDTO request, UriComponentsBuilder uriComponentsBuilder) {
        ResponseClientDTO responseClientDTO = clientService.save(request);
        URI uri = uriComponentsBuilder.path("/clients/{cpf}").buildAndExpand(responseClientDTO.getCpf()).toUri();
        return ResponseEntity.created(uri).body(responseClientDTO);
    }

    @GetMapping
    public ResponseEntity<List<ResponseClientDTO>> get(ResponseClientDTO response) {
        List<ResponseClientDTO> responseClientDTOS = clientService.get();
        return ResponseEntity.ok(responseClientDTOS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseClientDTO> update(@RequestBody @Valid RequestClientDTO request, @PathVariable String cpf) {
        clientService.update(request, cpf);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        clientService.delete(cpf);
        return ResponseEntity.noContent().build();
    }
}
=======

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

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }

}
>>>>>>> origin/master
