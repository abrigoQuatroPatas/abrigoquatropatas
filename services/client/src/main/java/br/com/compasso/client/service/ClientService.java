package br.com.compasso.client.service;

import br.com.compasso.client.dto.request.RequestClientDto;
import br.com.compasso.client.dto.request.RequestClientPutDto;
import br.com.compasso.client.dto.response.ResponseClientDto;
import br.com.compasso.client.dto.response.ZipCodeResponse;
import br.com.compasso.client.entity.Address;
import br.com.compasso.client.entity.ClientEntity;
import br.com.compasso.client.enums.Status;
import br.com.compasso.client.exception.NotStatusNull;
import br.com.compasso.client.http.ZipCodeClient;
import br.com.compasso.client.repository.ClientRepository;
import br.com.compasso.client.validation.Validations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ClientService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ZipCodeClient zipCodeClient;
    public ResponseClientDto post(RequestClientDto client) {

        ClientEntity clientEntity = modelMapper.map(client, ClientEntity.class);
        if (clientRepository.existsById(clientEntity.getCpf())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }
        String zipCode = client.getAddress().getZipCode()
                .replaceAll("\\D", "" );

        if (Validations.validateZipCode(zipCode)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Invalid zipCode!");
        }

        ZipCodeResponse zipCodeResponse;
        try{
            zipCodeResponse = zipCodeClient.findAddressByClient(zipCode).block();
        }catch (ResponseStatusException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Address address = Address.builder()
                .state(zipCodeResponse.getState())
                .city(zipCodeResponse.getCity())
                .district(zipCodeResponse.getDistrict())
                .street(zipCodeResponse.getStreet())
                .number(client.getAddress().getNumber())
                .build();
        clientEntity.setAddress(address);
        clientEntity.getAddress().setZipCode(zipCode.replaceAll("\\D", ""));
        clientEntity.setCpf(client.getCpf().replaceAll("\\D", ""));
        ClientEntity save = clientRepository.save(clientEntity);
        return modelMapper.map(save, ResponseClientDto.class);
    }
    public Page<ResponseClientDto> getAll(Pageable pageable) {
        Page<ClientEntity> all = clientRepository.findAll(pageable);
        return all.map(clientEntity -> modelMapper.map(clientEntity, ResponseClientDto.class));
    }
    public ResponseClientDto get(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(clientEntity, ResponseClientDto.class);
    }
    public void update(String cpf, RequestClientPutDto client) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(client, clientEntity);
        clientRepository.save(clientEntity);
    }
    public void putStatusInProgress(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatus(Status.IN_PROGRESS);
        clientRepository.save(clientEntity);
    }
    public void putStatusDisapproved(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatus(Status.DISAPPROVED);
        clientRepository.save(clientEntity);
    }
    public void putStatusNull(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatus(null);
        clientRepository.save(clientEntity);
    }
    public void putStatusApproved(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatus(Status.APPROVED);
        clientRepository.save(clientEntity);
    }
    public void delete(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (clientEntity.getStatus() != null) {
            throw new NotStatusNull();
        }
        clientRepository.delete(clientEntity);
    }
}