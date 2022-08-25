package com.compass.volunteer.service;

import com.compass.volunteer.dto.request.RequestVolunteerDto;
import com.compass.volunteer.dto.response.ResponseVolunteerDto;
import com.compass.volunteer.entity.VolunteerEntity;
import com.compass.volunteer.repository.VolunteerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<ResponseVolunteerDto> getAll(Pageable pageable) {
        Page<VolunteerEntity> all = repository.findAll(pageable);
        return all.map(volunteer -> modelMapper.map(volunteer, ResponseVolunteerDto.class));
    }

    public ResponseVolunteerDto get(String cpf) {
        VolunteerEntity volunteerEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(volunteerEntity, ResponseVolunteerDto.class);
    }

    public ResponseVolunteerDto post(RequestVolunteerDto volunteer) {
        VolunteerEntity volunteerEntity = modelMapper.map(volunteer, VolunteerEntity.class);
        if (repository.existsById(volunteerEntity.getCpf())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }
        VolunteerEntity save = repository.save(volunteerEntity);
        return modelMapper.map(save, ResponseVolunteerDto.class);
    }

    public void update(String cpf, RequestVolunteerDto volunteer) {
        VolunteerEntity volunteerEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(volunteer, volunteerEntity);
        repository.save(volunteerEntity);
    }

    public void delete(String cpf) {
        VolunteerEntity volunteerEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        repository.delete(volunteerEntity);
    }

}
