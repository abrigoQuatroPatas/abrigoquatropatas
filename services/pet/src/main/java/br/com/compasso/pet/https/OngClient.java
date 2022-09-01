package br.com.compasso.pet.https;

import br.com.compasso.pet.enums.TypeEnum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ong-ms")
public interface OngClient {
    @RequestMapping(method = RequestMethod.PUT, value = "/ong/{cnpj}/amount/{type}")
    void putAmount(@PathVariable String cnpj, @PathVariable TypeEnum type);

}

