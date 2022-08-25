package br.com.compasso.voluntary.controller;

import com.compass.volunteer.dto.request.RequestVolunteerDto;
import com.compass.volunteer.dto.response.ResponseVolunteerDto;
import com.compass.volunteer.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/VOLUNTEER")
public class VoluntaryController {

    @Autowired
    private VolunteerService service;

    @GetMapping
    public ResponseEntity<Page<ResponseVolunteerDto>> getAll(Pageable pageable) {
        Page<ResponseVolunteerDto> volunteers = service.getAll(pageable);
        return ResponseEntity.ok(volunteers);
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<ResponseVolunteerDto> get(@PathVariable String cpf) {
        ResponseVolunteerDto volunteer = service.get(cpf);
        return ResponseEntity.ok(volunteer);
    }

    @PostMapping
    public ResponseEntity<ResponseVolunteerDto> post(@RequestBody @Valid RequestVolunteerDto volunteer, UriComponentsBuilder componentsBuilder) {
        ResponseVolunteerDto volunteerDto = service.post(volunteer);
        URI uri = componentsBuilder.path("/VOLUNTEER/{cpf}").buildAndExpand(volunteerDto.getCpf()).toUri();
        return ResponseEntity.created(uri).body(volunteerDto);
    }

    @PutMapping("/{cpf}")
    public ResponseEntity<Void> put(@PathVariable String cpf, @RequestBody @Valid RequestVolunteerDto volunteer) {
        service.update(cpf, volunteer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cpf}")
    public ResponseEntity<Void> delete(@PathVariable String cpf) {
        service.delete(cpf);
        return ResponseEntity.noContent().build();
    }

}
