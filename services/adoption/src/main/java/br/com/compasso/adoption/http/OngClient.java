package br.com.compasso.adoption.http;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ong-ms")
public interface OngClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/ong/{cnpj}/pet/{type}")
    void putAmountPet(@PathVariable String cnpj, @PathVariable String type);

}
