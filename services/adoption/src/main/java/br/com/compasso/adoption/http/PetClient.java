package br.com.compasso.adoption.http;

import br.com.compasso.adoption.dto.response.ResponsePetDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("pet-ms")
public interface PetClient {

    @RequestMapping(method = RequestMethod.GET, value = "/pet/{id}")
    ResponsePetDto getPet(@PathVariable String id);

}
