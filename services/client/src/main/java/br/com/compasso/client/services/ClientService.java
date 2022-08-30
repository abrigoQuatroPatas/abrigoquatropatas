package br.com.compasso.client.services;

<<<<<<< HEAD
import br.com.compasso.client.dtos.request.RequestClientDTO;
import br.com.compasso.client.dtos.response.ResponseClientDTO;
import br.com.compasso.client.entities.ClientEntity;
import br.com.compasso.client.repositorys.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

=======
import br.com.compasso.client.dtos.request.RequestClientDto;
import br.com.compasso.client.dtos.response.ResponseClientDto;
import br.com.compasso.client.entitys.ClientEntity;
import br.com.compasso.client.repositorys.ClientRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
>>>>>>> origin/master

@Service
public class ClientService {

    @Autowired
<<<<<<< HEAD
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

=======
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

    public void delete(String cpf) {
        ClientEntity clientEntity = clientRepository.findById(cpf).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        clientRepository.delete(clientEntity);
    }
>>>>>>> origin/master
}
