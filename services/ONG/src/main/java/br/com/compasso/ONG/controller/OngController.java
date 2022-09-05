package br.com.compasso.ONG.controller;

import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.dto.response.ResponseOngDto;
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
        URI uri = component.path("/ONG/{cnpj}").buildAndExpand(ongCadastrada.getCnpj()).toUri();
        return ResponseEntity.created(uri).body(ongCadastrada);
    }

    @GetMapping
    public ResponseEntity<Page<ResponseOngDto>> getAll(Pageable pageable) {
        Page<ResponseOngDto> ongs = service.getAll(pageable);
        return ResponseEntity.ok(ongs);
    }

    @GetMapping("/{cnpj}")
    public ResponseEntity<ResponseOngDto> get(@PathVariable String cnpj) {
        ResponseOngDto ong = service.get(cnpj);
        return ResponseEntity.ok(ong);
    }

    @PutMapping("/{cnpj}")
    public ResponseEntity<Void> put(@PathVariable String cnpj, @RequestBody RequestOngDto ong) {
        service.update(cnpj, ong);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cnpj}/pet/{type}")
    public ResponseEntity<Void> putAmountLess(@PathVariable String cnpj, @PathVariable String type) {
        service.updateAmountLess(cnpj, type);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cnpj}")
    public ResponseEntity<Void> delete(@PathVariable String cnpj) {
        service.delete(cnpj);
        return ResponseEntity.noContent().build();
    }

}
