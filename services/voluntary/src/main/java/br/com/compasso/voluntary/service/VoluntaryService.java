package br.com.compasso.voluntary.service;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.response.ResponseOngDto;
import br.com.compasso.voluntary.dto.response.ResponseVoluntaryDto;
import br.com.compasso.voluntary.entity.VoluntaryEntity;
import br.com.compasso.voluntary.http.OngClient;
import br.com.compasso.voluntary.repository.VoluntaryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class VoluntaryService {

    @Autowired
    private VoluntaryRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OngClient client;

    public Page<ResponseVoluntaryDto> getAll(Pageable pageable) {
        Page<VoluntaryEntity> all = repository.findAll(pageable);
        List<ResponseVoluntaryDto> responseVoluntaryList = new ArrayList<>();
        all.forEach(volunteer -> {
            ResponseOngDto ong = client.getOng(volunteer.getOngId());
            ResponseVoluntaryDto responseVoluntaryDto = ResponseVoluntaryDto.builder()
                    .cpf(volunteer.getCpf())
                    .name(volunteer.getName())
                    .type(volunteer.getType())
                    .birthDate(volunteer.getBirthDate())
                    .address(volunteer.getAddress())
                    .status(volunteer.getStatus())
                    .ong(ong)
                    .build();
            responseVoluntaryList.add(responseVoluntaryDto);
        });
        return new PageImpl<>(responseVoluntaryList);
    };

    public ResponseVoluntaryDto get(String cpf) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ResponseOngDto ong = client.getOng(voluntaryEntity.getOngId());
        ResponseVoluntaryDto responseVoluntary = modelMapper.map(voluntaryEntity, ResponseVoluntaryDto.class);
        responseVoluntary.setOng(ong);
        return responseVoluntary;
    }

    public ResponseVoluntaryDto post(RequestVoluntaryDto volunteer) {
        VoluntaryEntity VoluntaryEntity = modelMapper.map(volunteer, VoluntaryEntity.class);
        if (repository.existsById(VoluntaryEntity.getCpf())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }
        VoluntaryEntity save = repository.save(VoluntaryEntity);
        return modelMapper.map(save, ResponseVoluntaryDto.class);
    }

    public void update(String cpf, RequestVoluntaryDto volunteer) {
        VoluntaryEntity VoluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(volunteer, VoluntaryEntity);
        repository.save(VoluntaryEntity);
    }

    public void delete(String cpf) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        client.deleteVoluntary(voluntaryEntity.getOngId(), voluntaryEntity.getCpf());
        repository.delete(voluntaryEntity);
    }

    public void addVoluntary(String cpf, String cnpj){
        VoluntaryEntity voluntary = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        voluntary.setOngId(cnpj);
        client.addVoluntary(cnpj, cpf);
        repository.save(voluntary);
    }

    public List<ResponseVoluntaryDto> getByOngId(String cnpj) {
        List<VoluntaryEntity> voluntaryEntity = repository.findByOngId(cnpj);
        List<ResponseVoluntaryDto> collect = new ArrayList<>();
        voluntaryEntity.forEach(volunteer -> {
            ResponseOngDto ong = client.getOng(cnpj);
            ResponseVoluntaryDto voluntaryDto = ResponseVoluntaryDto.builder()
                    .cpf(volunteer.getCpf())
                    .name(volunteer.getName())
                    .type(volunteer.getType())
                    .birthDate(volunteer.getBirthDate())
                    .address(volunteer.getAddress())
                    .status(volunteer.getStatus())
                    .ong(ong)
                    .build();
            collect.add(voluntaryDto);
        });
        return collect;
    }

}
