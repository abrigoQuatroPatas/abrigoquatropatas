package br.com.compasso.voluntary.service;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.response.ResponseOngDto;
import br.com.compasso.voluntary.dto.response.ResponseVoluntaryDto;
import br.com.compasso.voluntary.entity.AddressEntity;
import br.com.compasso.voluntary.entity.VoluntaryEntity;
import br.com.compasso.voluntary.http.OngClient;
import br.com.compasso.voluntary.http.ZipCodeClient;
import br.com.compasso.voluntary.repository.VoluntaryRepository;
import br.com.compasso.voluntary.dto.response.ZipCodeResponse;
import br.com.compasso.voluntary.validation.Validations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoluntaryService {

    @Autowired
    private VoluntaryRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OngClient client;

    @Autowired
    private ZipCodeClient zipCodeClient;

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
    }

    public ResponseVoluntaryDto get(String cpf) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ResponseOngDto ong = client.getOng(voluntaryEntity.getOngId());
        ResponseVoluntaryDto responseVoluntary = modelMapper.map(voluntaryEntity, ResponseVoluntaryDto.class);
        responseVoluntary.setOng(ong);
        return responseVoluntary;
    }

    public ResponseVoluntaryDto post(RequestVoluntaryDto volunteer) {
        VoluntaryEntity voluntaryEntity = modelMapper.map(volunteer, VoluntaryEntity.class);
        if (repository.existsById(voluntaryEntity.getCpf())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }

        String zipCode = volunteer.getAddress().getZipCode()
                .replaceAll("\\D", "" );

        if (Validations.validateZipCode(zipCode)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Invalid zipCode!");
        }

        ZipCodeResponse zipCodeResponse = zipCodeClient.findAddressByVolunteeer(zipCode).block();

        AddressEntity address = AddressEntity.builder()
                .state(zipCodeResponse.getState())
                .city(zipCodeResponse.getCity())
                .district(zipCodeResponse.getDistrict())
                .street(zipCodeResponse.getStreet())
                .number(volunteer.getAddress().getNumber())
                .build();

        voluntaryEntity.setAddress(address);
        voluntaryEntity.getAddress().setZipCode(zipCode.replaceAll("\\D", ""));

        voluntaryEntity.setCpf(volunteer.getCpf().replaceAll("\\D", ""));

        VoluntaryEntity save = repository.save(voluntaryEntity);
        return modelMapper.map(save, ResponseVoluntaryDto.class);
    }

    public void update(String cpf, RequestVoluntaryDto volunteer) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(volunteer, voluntaryEntity);
        repository.save(voluntaryEntity);
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
