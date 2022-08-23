package br.com.compasso.client.controllers;


import br.com.compasso.client.dtos.request.RequestClientDTO;
import br.com.compasso.client.dtos.response.ResponseClientDTO;
import br.com.compasso.client.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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