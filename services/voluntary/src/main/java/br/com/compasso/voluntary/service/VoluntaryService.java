package br.com.compasso.voluntary.service;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.response.ResponseVoluntaryDto;
import br.com.compasso.voluntary.entity.VoluntaryEntity;
import br.com.compasso.voluntary.httpclient.ZipCodeClient;
import br.com.compasso.voluntary.repository.VoluntaryRepository;
import br.com.compasso.voluntary.response.ZipCodeResponse;
import br.com.compasso.voluntary.validations.Validations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VoluntaryService {

    @Autowired
    private VoluntaryRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ZipCodeClient zipCodeClient;

    public Page<ResponseVoluntaryDto> getAll(Pageable pageable) {
        Page<VoluntaryEntity> all = repository.findAll(pageable);
        return all.map(volunteer -> modelMapper.map(volunteer, ResponseVoluntaryDto.class));
    }

    public ResponseVoluntaryDto get(String cpf) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(voluntaryEntity, ResponseVoluntaryDto.class);
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

        voluntaryEntity.getAddress().setState(zipCodeResponse.getState());
        voluntaryEntity.getAddress().setCity(zipCodeResponse.getCity());
        voluntaryEntity.getAddress().setDistrict(zipCodeResponse.getDistrict());
        voluntaryEntity.getAddress().setStreet(zipCodeResponse.getStreet());

        voluntaryEntity.getAddress().setZipCode(zipCode.replaceAll("\\D", ""));

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
        repository.delete(voluntaryEntity);
    }

}