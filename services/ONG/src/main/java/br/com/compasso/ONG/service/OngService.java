package br.com.compasso.ONG.service;

import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.dto.response.ResponseOngDto;
import br.com.compasso.ONG.dto.response.ResponseOngVolunteersDto;
import br.com.compasso.ONG.dto.response.ResponseVoluntaryDto;
import br.com.compasso.ONG.entity.Address;
import br.com.compasso.ONG.entity.OngEntity;
import br.com.compasso.ONG.http.VoluntaryClient;
import br.com.compasso.ONG.httpclient.ZipCodeClient;
import br.com.compasso.ONG.repository.OngRepository;
import br.com.compasso.ONG.response.ZipCodeResponse;
import br.com.compasso.ONG.validations.Validations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OngService {

    @Autowired
    private OngRepository ongRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ZipCodeClient zipCodeClient;
    @Autowired
    private VoluntaryClient voluntaryClient;

    public ResponseOngDto post(RequestOngDto ong) {
        OngEntity ongEntity = modelMapper.map(ong, OngEntity.class);
        if (ongRepository.existsById(ong.getCnpj())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }

        String zipCode = ong.getAddress().getZipCode().replaceAll("\\D", "" );

        if (Validations.validateZipCode(zipCode)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Invalid zipCode!");
        }

        ZipCodeResponse zipCodeResponse = zipCodeClient.findAddressByOng(zipCode).block();

        Address address = Address.builder()
                .state(zipCodeResponse.getState())
                .city(zipCodeResponse.getCity())
                .district(zipCodeResponse.getDistrict())
                .street(zipCodeResponse.getStreet())
                .number(ong.getAddress().getNumber())
                .build();

        ongEntity.setAddress(address);
        ongEntity.getAddress().setZipCode(zipCode.replaceAll("\\D", ""));

        ongEntity.setCnpj(ong.getCnpj().replaceAll("\\D", ""));

        OngEntity save = ongRepository.save(ongEntity);

        return modelMapper.map(save, ResponseOngDto.class);
    }

    public Page<ResponseOngDto> getAll(Pageable pageable) {
        Page<OngEntity> all = ongRepository.findAll(pageable);
        return all.map(ong -> modelMapper.map(ong, ResponseOngDto.class));
    }

    public ResponseOngVolunteersDto getWithVoluntaries(String cnpj) {
        OngEntity ongEntity = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<ResponseVoluntaryDto> ongId = voluntaryClient.getOngId(ongEntity.getCnpj());
        ResponseOngVolunteersDto ong = modelMapper.map(ongEntity, ResponseOngVolunteersDto.class);
        ong.setVoluntaries(ongId);
        return ong;
    }

    public ResponseOngDto getWithoutVoluntaries(String cnpj) {
        OngEntity ongEntity = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        return modelMapper.map(ongEntity, ResponseOngDto.class);
    }

    public void update(String cnpj, RequestOngDto ong) {
        OngEntity ongEntity = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        modelMapper.map(ong, ongEntity);
        ongRepository.save(ongEntity);
    }

    public void delete(String cnpj) {
        OngEntity ongEntity = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ongRepository.delete(ongEntity);
    }

    public void putAmount(String cnpj, String type) {
        OngEntity ong = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(type.equals("dog")) {
            Integer amountDog = ong.getAmountDog();
            ong.setAmountDog(amountDog + 1);
            ongRepository.save(ong);
        } else if (type.equals("cat")) {
            Integer amountCat = ong.getAmountCat();
            ong.setAmountCat(amountCat + 1);
            ongRepository.save(ong);
        }
    }

    public void deleteAmount(String cnpj, String type) {
        OngEntity ong = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(type.equals("dog")) {
            Integer amountDog = ong.getAmountDog();
            ong.setAmountDog(amountDog - 1);
            ongRepository.save(ong);

        } else if (type.equals("cat")) {
            Integer amountCat = ong.getAmountCat();
            ong.setAmountCat(amountCat - 1);
            ongRepository.save(ong);
        }
    }

    public void deleteVoluntary(String cnpj, String cpf) {
        OngEntity ongEntity = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ongEntity.getVoluntaryIds().remove(cpf);
    }

    public void updateVoluntary(String cnpj, String cpf) {
        OngEntity ongEntity = ongRepository.findById(cnpj).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ongEntity.getVoluntaryIds().add(cpf);
        ongRepository.save(ongEntity);
    }
}
