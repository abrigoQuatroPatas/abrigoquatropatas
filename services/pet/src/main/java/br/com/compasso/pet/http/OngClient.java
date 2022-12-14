package br.com.compasso.pet.http;

import br.com.compasso.pet.dto.response.ResponseOngDto;
import br.com.compasso.pet.repository.PetRepository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ong-ms")
public interface OngClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/ong/{cnpj}/amount/{type}")
    void putAmount(@PathVariable String cnpj, @PathVariable String type);

    @RequestMapping(method = RequestMethod.DELETE, value = "/ong/{cnpj}/amount/{type}")
    PetRepository deleteAmount(@PathVariable String cnpj, @PathVariable String type);

    @RequestMapping(method = RequestMethod.GET, value = "/ong/{cnpj}")
    ResponseOngDto getOng(@PathVariable String cnpj);
}

