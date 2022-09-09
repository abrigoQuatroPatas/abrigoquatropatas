package br.com.compasso.pet.http;

import br.com.compasso.pet.dto.response.ResponseAdoptionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("adoption-ms")
public interface AdoptionClient {

    @RequestMapping(method = RequestMethod.GET, value = "/adoption")
    List<ResponseAdoptionDto> getAdoption();

}
