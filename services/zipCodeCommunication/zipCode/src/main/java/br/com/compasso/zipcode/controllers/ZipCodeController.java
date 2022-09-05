package br.com.compasso.zipcode.controllers;

import br.com.compasso.zipcode.responses.ZipCodeResponse;
import br.com.compasso.zipcode.services.ZipCodeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/webclient")
public class ZipCodeController {
        private ZipCodeService zipCodeService;

        @GetMapping("{zipCode}/json")
        @ResponseStatus(HttpStatus.OK)
        public Mono<ZipCodeResponse> getZipCodeByCep (@PathVariable String zipCode){
            return zipCodeService.findZipCodeByCep(zipCode);
        }
}
