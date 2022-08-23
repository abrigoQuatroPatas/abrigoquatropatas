package br.com.compasso.client.services;

import br.com.compasso.client.dtos.request.RequestClientDTO;
import br.com.compasso.client.dtos.response.ResponseClientDTO;
import br.com.compasso.client.entities.ClientEntity;
import br.com.compasso.client.repositorys.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ResponseClientDTO save(RequestClientDTO requestClientDTO) {
        ClientEntity entity = modelMapper.map(requestClientDTO, ClientEntity.class);
        ClientEntity saved = clientRepository.save(entity);
        return modelMapper.map(saved, ResponseClientDTO.class);
    }

    public List<ResponseClientDTO> get() {
        List<ClientEntity> allClients = clientRepository.findAll();
        List<ResponseClientDTO> dtos = allClients.stream().map(clientEntity ->
                modelMapper.map(clientEntity, ResponseClientDTO.class)).collect(Collectors.toList());
        return dtos;
    }

    public void delete(String cpf) {
        clientRepository.findById(cpf);
        clientRepository.deleteById(cpf);
    }

    public void update(RequestClientDTO request, String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElse(null);
        modelMapper.map(request, clientEntity);
        clientRepository.save(clientEntity);
    }

}
