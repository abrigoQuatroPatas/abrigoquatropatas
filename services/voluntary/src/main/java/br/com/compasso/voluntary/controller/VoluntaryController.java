package br.com.compasso.voluntary.controller;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
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
        Page<ResponseVoluntaryDto> volunteers = service.getAll(pageable);
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ResponseVoluntaryDto> get(@PathVariable String cpf) {
        ResponseVoluntaryDto volunteer = service.get(cpf);
        return ResponseEntity.ok(volunteer);
    }

    @PostMapping
    public ResponseEntity<ResponseVoluntaryDto> post(@RequestBody @Valid RequestVoluntaryDto volunteer, UriComponentsBuilder componentsBuilder) {
        ResponseVoluntaryDto volunteerDto = service.post(volunteer);
        URI uri = componentsBuilder.path("/voluntary/{cpf}").buildAndExpand(volunteerDto.getCpf()).toUri();
        return ResponseEntity.created(uri).body(volunteerDto);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Void> put(@PathVariable String cpf, @RequestBody @Valid RequestVoluntaryDto volunteer) {
        service.update(cpf, volunteer);
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
        List<ResponseVoluntaryDto> volunteer = service.getByOngId(cnpj);
        return ResponseEntity.ok(volunteer);
    }

}
