package br.com.compasso.zipcode.controller;

import br.com.compasso.zipcode.dto.response.ZipCodeResponse;
import br.com.compasso.zipcode.service.ZipCodeService;
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
