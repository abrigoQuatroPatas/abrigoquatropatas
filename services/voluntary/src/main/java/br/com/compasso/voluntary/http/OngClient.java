package br.com.compasso.voluntary.http;

import br.com.compasso.voluntary.dto.response.ResponseOngDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ong-ms")
public interface OngClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/ong/{cnpj}/voluntary/{cpf}")
    void addVoluntary(@PathVariable String cnpj, @PathVariable String cpf);

    @RequestMapping(method = RequestMethod.GET, value = "/ong/{cnpj}")
    ResponseOngDto getOng(@PathVariable String cnpj);

    @RequestMapping(method = RequestMethod.DELETE, value = "/ong/{cnpj}/voluntary/{cpf}")
    void deleteVoluntary(@PathVariable String cnpj, @PathVariable String cpf);
}
