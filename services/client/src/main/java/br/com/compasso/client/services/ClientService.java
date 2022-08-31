package br.com.compasso.client.services;

import br.com.compasso.client.dtos.request.RequestClientDto;
import br.com.compasso.client.dtos.response.ResponseClientDto;
import br.com.compasso.client.entitys.ClientEntity;
import br.com.compasso.client.enums.StatusEnum;
import br.com.compasso.client.repositorys.ClientRepository;
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

    public ResponseClientDto post(RequestClientDto client) {
        ClientEntity clientEntity = modelMapper.map(client, ClientEntity.class);
        if (clientRepository.existsById(clientEntity.getCpf())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }
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

    public void update(String cpf, RequestClientDto client) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(client, clientEntity);
        clientRepository.save(clientEntity);
    }

    public void putStatusInProgress(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatusEnum(StatusEnum.IN_PROGRESS);
        clientRepository.save(clientEntity);
    }

    public void putStatusDisapproved(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatusEnum(StatusEnum.DISAPPROVED);
        clientRepository.save(clientEntity);
    }

    public void putStatusNull(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatusEnum(null);
        clientRepository.save(clientEntity);
    }

    public void putStatusApproved(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientEntity.setStatusEnum(StatusEnum.APPROVED);
        clientRepository.save(clientEntity);
    }

    public void delete(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientRepository.delete(clientEntity);
    }
}
