package br.com.compasso.ONG.http;

import br.com.compasso.ONG.dto.response.ResponsePetDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("pet-ms")
public interface PetClient {

    @RequestMapping(method = RequestMethod.GET, value = "/pet/ong/{cnpj}")
    List<ResponsePetDto> getOngId(@PathVariable String cnpj);
}
