package br.com.compasso.ONG.http;

import br.com.compasso.ONG.dto.response.ResponseVoluntaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("voluntary-ms")
public interface VoluntaryClient {


    @RequestMapping(method = RequestMethod.GET, value = "/voluntary/ong/{cnpj}")
    List<ResponseVoluntaryDto> getOngId(@PathVariable String cnpj);

}
