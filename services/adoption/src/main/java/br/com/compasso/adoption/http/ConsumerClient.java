package br.com.compasso.adoption.http;

import br.com.compasso.adoption.dto.response.ResponseConsumerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("client-ms")
public interface ConsumerClient {

    @RequestMapping(method = RequestMethod.GET, value = "/client/{cpf}")
    ResponseConsumerDto getConsumer(@PathVariable String cpf);

    @RequestMapping(method = RequestMethod.PUT, value = "/client/{cpf}/inProgress")
    void putStatusConsumerInProgress(@PathVariable String cpf);

    @RequestMapping(method = RequestMethod.PUT, value = "/client/{cpf}/approved")
    void putStatusConsumerApproved(@PathVariable String cpf);

    @RequestMapping(method = RequestMethod.PUT, value = "/client/{cpf}/disapproved")
    void putStatusConsumerDisapproved(@PathVariable String cpf);

    @RequestMapping(method = RequestMethod.PUT, value = "/client/{cpf}/null")
    void putStatusConsumerNull(@PathVariable String cpf);
}
