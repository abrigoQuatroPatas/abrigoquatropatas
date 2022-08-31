package br.com.compasso.adoption.controller;

import br.com.compasso.adoption.dto.response.ResponseAdoptionDto;
import br.com.compasso.adoption.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/adoption")
public class AdoptionController {

    @Autowired
    private AdoptionService service;

    @PostMapping("/consumer/{consumerId}/pet/{petId}")
    public ResponseEntity<ResponseAdoptionDto> post(@PathVariable String consumerId, @PathVariable String petId,
                                                    UriComponentsBuilder componentsBuilder) {
        ResponseAdoptionDto adoptionDto = service.post(consumerId, petId);

        URI uri = componentsBuilder.path("/adoption/{id}/consumer/{consumerId}/pet/{petId}")
                .buildAndExpand(adoptionDto.getId(), adoptionDto.getConsumer().getCpf(), adoptionDto.getPet().getId()).toUri();

        return ResponseEntity.created(uri).body(adoptionDto);
    }

    @GetMapping
    public ResponseEntity<List<ResponseAdoptionDto>> get() {
        List<ResponseAdoptionDto> adoptions = service.get();
        return ResponseEntity.ok(adoptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAdoptionDto> get(@PathVariable Long id) {
        ResponseAdoptionDto adoption = service.get(id);
        return ResponseEntity.ok(adoption);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/consumer/{idConsumer}/approved")
    public ResponseEntity<Void> putStatusApproved(@PathVariable String idConsumer) {
        service.putStatusConsumerApproved(idConsumer);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/consumer/{idConsumer}/disapproved")
    public ResponseEntity<Void> putStatusDisapproved(@PathVariable String idConsumer) {
        service.putStatusConsumerDisapproved(idConsumer);
        return ResponseEntity.noContent().build();
    }

}
