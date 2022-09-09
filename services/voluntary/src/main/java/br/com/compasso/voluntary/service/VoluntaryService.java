package br.com.compasso.voluntary.service;

import br.com.compasso.voluntary.dto.request.RequestVoluntaryDto;
import br.com.compasso.voluntary.dto.request.RequestVoluntaryPutDto;
import br.com.compasso.voluntary.dto.response.ResponseAddressDto;
import br.com.compasso.voluntary.dto.response.ResponseOngDto;
import br.com.compasso.voluntary.dto.response.ResponseVoluntaryDto;
import br.com.compasso.voluntary.entity.AddressEntity;
import br.com.compasso.voluntary.entity.VoluntaryEntity;
import br.com.compasso.voluntary.exceptions.MessageFeignException;
import br.com.compasso.voluntary.http.OngClient;
import br.com.compasso.voluntary.httpclient.ZipCodeClient;
import br.com.compasso.voluntary.repository.VoluntaryRepository;
import br.com.compasso.voluntary.response.ZipCodeResponse;
import br.com.compasso.voluntary.validations.Validations;
import feign.Feign;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        try {
            all.forEach(volunteer -> {
                ResponseAddressDto address = modelMapper.map(volunteer.getAddress(), ResponseAddressDto.class);
                ResponseOngDto ong = client.getOng(volunteer.getOngId());
                ResponseVoluntaryDto responseVoluntaryDto = ResponseVoluntaryDto.builder()
                        .cpf(volunteer.getCpf())
                        .name(volunteer.getName())
                        .type(volunteer.getType())
                        .birthDate(volunteer.getBirthDate())
                        .address(address)
                        .status(volunteer.getStatus())
                        .ong(ong)

                        .build();
                responseVoluntaryList.add(responseVoluntaryDto);
            });
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }
        return new PageImpl<>(responseVoluntaryList);
    }

    public ResponseVoluntaryDto get(String cpf) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            ResponseOngDto ong = client.getOng(voluntaryEntity.getOngId());
            ResponseVoluntaryDto responseVoluntary = modelMapper.map(voluntaryEntity, ResponseVoluntaryDto.class);
            responseVoluntary.setOng(ong);
            return responseVoluntary;
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }
    }

    public ResponseVoluntaryDto post(RequestVoluntaryDto volunteer, String ongId) {
        VoluntaryEntity voluntaryEntity = modelMapper.map(volunteer, VoluntaryEntity.class);
        ResponseOngDto ong;
        try {
            if (repository.existsById(voluntaryEntity.getCpf())) {
                throw new ResponseStatusException(HttpStatus.OK);
            }

            String zipCode = volunteer.getAddress().getZipCode()
                    .replaceAll("\\D", "");

            if (Validations.validateZipCode(zipCode)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid zipCode!");
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
            voluntaryEntity.setOngId(ongId);
            voluntaryEntity.setCpf(volunteer.getCpf().replaceAll("\\D", ""));
            client.addVoluntary(ongId, volunteer.getCpf());
            ong = client.getOng(ongId);
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }

        VoluntaryEntity save = repository.save(voluntaryEntity);
        ResponseAddressDto address = modelMapper.map(volunteer.getAddress(), ResponseAddressDto.class);
        return ResponseVoluntaryDto.builder()
                .cpf(volunteer.getCpf())
                .name(volunteer.getName())
                .type(volunteer.getType())
                .birthDate(volunteer.getBirthDate())
                .address(address)
                .status(volunteer.getStatus())
                .ong(ong)
                .build();
    }

    public void update(String cpf, RequestVoluntaryPutDto volunteer) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(volunteer, voluntaryEntity);
        repository.save(voluntaryEntity);
    }

    public void delete(String cpf) {
        VoluntaryEntity voluntaryEntity = repository.findById(cpf).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            client.deleteVoluntary(voluntaryEntity.getOngId(), voluntaryEntity.getCpf());
            repository.delete(voluntaryEntity);
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }
    }

    public List<ResponseVoluntaryDto> getByOngId(String cnpj) {
        List<VoluntaryEntity> voluntaryEntity = repository.findByOngId(cnpj);
        List<ResponseVoluntaryDto> collect = new ArrayList<>();
       try {
           voluntaryEntity.forEach(volunteer -> {
               ResponseOngDto ong = client.getOng(cnpj);
               ResponseAddressDto address = modelMapper.map(volunteer.getAddress(), ResponseAddressDto.class);
               ResponseVoluntaryDto voluntaryDto = ResponseVoluntaryDto.builder()
                       .cpf(volunteer.getCpf())
                       .name(volunteer.getName())
                       .type(volunteer.getType())
                       .birthDate(volunteer.getBirthDate())
                       .address(address)
                       .status(volunteer.getStatus())
                       .ong(ong)
                       .build();
               collect.add(voluntaryDto);
           });
       } catch (FeignException e) {
           throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
       }
        return collect;
    }

}
