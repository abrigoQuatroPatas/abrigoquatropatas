package br.com.compasso.ONG.controller;

import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.dto.request.RequestOngPutDto;
import br.com.compasso.ONG.dto.response.ResponseOngDto;
import br.com.compasso.ONG.dto.response.ResponseOngVolunteersDto;
import br.com.compasso.ONG.service.OngService;
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
public class OngController {

    @Autowired
    private OngService service;

    @PostMapping
    public ResponseEntity<ResponseOngDto> post(@RequestBody @Valid RequestOngDto ong, UriComponentsBuilder component) {
        ResponseOngDto ongCadastrada = service.post(ong);
        URI uri = component.path("/ong/{cnpj}").buildAndExpand(ongCadastrada.getCnpj()).toUri();
        return ResponseEntity.created(uri).body(ongCadastrada);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseOngDto>> getAll(Pageable pageable) {
        Page<ResponseOngDto> ongs = service.getAll(pageable);
        return ResponseEntity.ok(ongs);
    }

    @GetMapping("/{cnpj}/voluntaries")
    public ResponseEntity<ResponseOngVolunteersDto> getWithVoluntaries(@PathVariable String cnpj) {
        ResponseOngVolunteersDto ong = service.getWithVoluntaries(cnpj);
        return ResponseEntity.ok(ong);
    }

    @GetMapping("/{cnpj}")
    public ResponseEntity<ResponseOngDto> getWithoutVoluntaries(@PathVariable String cnpj) {
        ResponseOngDto ong = service.getWithoutVoluntaries(cnpj);
        return ResponseEntity.ok(ong);
    }

    @PutMapping("/{cnpj}")
    public ResponseEntity<Void> put(@PathVariable String cnpj, @RequestBody RequestOngPutDto ong) {
        service.update(cnpj, ong);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cnpj}/amount/{type}")
    public ResponseEntity<Void> putAmount(@PathVariable String cnpj, @PathVariable String type) {
        service.putAmount(cnpj, type);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> delete(@PathVariable String cnpj) {
        service.delete(cnpj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cnpj}/voluntary/{cpf}")
    public ResponseEntity<Void> deleteVoluntary(@PathVariable String cnpj, @PathVariable String cpf) {
        service.deleteVoluntary(cnpj, cpf);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cnpj}/voluntary/{cpf}")
    public ResponseEntity<Void> putVoluntary(@PathVariable String cnpj, @PathVariable String cpf) {
        service.updateVoluntary(cnpj, cpf);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cnpj}/amount/{type}")
    public ResponseEntity<Void> deleteAmount(@PathVariable String cnpj, @PathVariable String type) {
        service.deleteAmount(cnpj, type);
        return ResponseEntity.noContent().build();
    }
}
