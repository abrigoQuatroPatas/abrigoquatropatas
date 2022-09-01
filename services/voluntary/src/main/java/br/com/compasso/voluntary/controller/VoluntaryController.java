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
import java.net.URI;

@RestController
@RequestMapping("/volunteer")
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
        URI uri = componentsBuilder.path("/volunteer/{cpf}").buildAndExpand(volunteerDto.getCpf()).toUri();
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

}
