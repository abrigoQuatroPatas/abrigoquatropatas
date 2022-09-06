package br.com.compasso.adoption.http;

import br.com.compasso.adoption.dto.response.ResponseOngDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ong-ms")
public interface OngClient {

    @RequestMapping(method = RequestMethod.DELETE, value = "/ong/{cnpj}/amount/{type}")
    void putAmountPet(@PathVariable String cnpj, @PathVariable String type);

    @RequestMapping(method = RequestMethod.GET, value = "/ong/{cnpj}")
    ResponseOngDto getOng(@PathVariable String cnpj);

    @RequestMapping(method = RequestMethod.PUT, value = "/ong/{cnpj}/amount/{type}")
    void putAmountPetPlus(@PathVariable String cnpj, @PathVariable String type);
}
