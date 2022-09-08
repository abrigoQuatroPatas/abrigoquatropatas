package br.com.compasso.voluntary.controller;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.request.RequestVoluntaryPutDto;
import br.com.compasso.voluntary.dto.response.ResponseVoluntaryDto;
import br.com.compasso.voluntary.service.VoluntaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/voluntary")
public class VoluntaryController {

    @Autowired
    private VoluntaryService service;

    @GetMapping
    public ResponseEntity<Page<ResponseVoluntaryDto>> getAll(Pageable pageable) {
        Page<ResponseVoluntaryDto> voluntaries = service.getAll(pageable);
        return ResponseEntity.ok(voluntaries);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ResponseVoluntaryDto> get(@PathVariable String cpf) {
        ResponseVoluntaryDto voluntary = service.get(cpf);
        return ResponseEntity.ok(voluntary);
    }

    @PostMapping
    public ResponseEntity<ResponseVoluntaryDto> post(@RequestBody @Valid RequestVoluntaryDto voluntary, UriComponentsBuilder componentsBuilder) {
        ResponseVoluntaryDto voluntaryDto = service.post(voluntary);
        URI uri = componentsBuilder.path("/voluntary/{cpf}").buildAndExpand(voluntaryDto.getCpf()).toUri();
        return ResponseEntity.created(uri).body(voluntaryDto);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Void> put(@PathVariable String cpf, @RequestBody @Valid RequestVoluntaryPutDto voluntary) {
        service.update(cpf, voluntary);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cpf}/ong/{cnpj}")
    public ResponseEntity<Void> addVoluntary(@PathVariable @NotNull String cpf, @PathVariable String cnpj) {
        service.addVoluntary(cpf,cnpj);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ong/{cnpj}")
    public ResponseEntity<List<ResponseVoluntaryDto>> getOngId(@PathVariable String cnpj) {
        List<ResponseVoluntaryDto> voluntary = service.getByOngId(cnpj);
        return ResponseEntity.ok(voluntary);
    }

}
