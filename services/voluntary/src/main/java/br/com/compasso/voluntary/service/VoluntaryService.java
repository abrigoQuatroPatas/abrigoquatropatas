package br.com.compasso.voluntary.service;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.response.ResponseVoluntaryDto;
import br.com.compasso.voluntary.entity.VoluntaryEntity;
import br.com.compasso.voluntary.httpclient.ZipCodeClient;
import br.com.compasso.voluntary.repository.VoluntaryRepository;
import br.com.compasso.voluntary.response.ZipCodeResponse;
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
        VoluntaryEntity VoluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(VoluntaryEntity, ResponseVoluntaryDto.class);
    }

    public ResponseVoluntaryDto post(RequestVoluntaryDto volunteer) {
        VoluntaryEntity VoluntaryEntity = modelMapper.map(volunteer, VoluntaryEntity.class);
        if (repository.existsById(VoluntaryEntity.getCpf())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }

        String zipCode = volunteer.getAddress().getZipCode();

        ZipCodeResponse zipCodeResponse = zipCodeClient.findAddressByVolunteeer(zipCode).block();

        VoluntaryEntity.getAddress().setState(zipCodeResponse.getState());
        VoluntaryEntity.getAddress().setCity(zipCodeResponse.getCity());
        VoluntaryEntity.getAddress().setDistrict(zipCodeResponse.getDistrict());
        VoluntaryEntity.getAddress().setStreet(zipCodeResponse.getStreet());

        VoluntaryEntity save = repository.save(VoluntaryEntity);
        return modelMapper.map(save, ResponseVoluntaryDto.class);
    }

    public void update(String cpf, RequestVoluntaryDto volunteer) {
        VoluntaryEntity VoluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(volunteer, VoluntaryEntity);
        repository.save(VoluntaryEntity);
    }

    public void delete(String cpf) {
        VoluntaryEntity VoluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repository.delete(VoluntaryEntity);
    }

}
