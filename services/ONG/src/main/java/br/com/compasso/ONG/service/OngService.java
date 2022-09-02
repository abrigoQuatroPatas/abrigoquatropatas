package br.com.compasso.ONG.service;

import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.dto.response.ResponseOngDto;
import br.com.compasso.ONG.entity.OngEntity;
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

@Service
public class OngService {

    @Autowired
    private OngRepository ongRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ZipCodeClient zipCodeClient;
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

        ongEntity.getAddress().setState(zipCodeResponse.getState());
        ongEntity.getAddress().setCity(zipCodeResponse.getCity());
        ongEntity.getAddress().setDistrict(zipCodeResponse.getDistrict());
        ongEntity.getAddress().setStreet(zipCodeResponse.getStreet());

        ongEntity.getAddress().setZipCode(zipCode.replaceAll("\\D", ""));

        OngEntity save = ongRepository.save(ongEntity);

        return modelMapper.map(save, ResponseOngDto.class);
    }

    public Page<ResponseOngDto> getAll(Pageable pageable) {
        Page<OngEntity> all = ongRepository.findAll(pageable);
        return all.map(ong -> modelMapper.map(ong, ResponseOngDto.class));
    }

    public ResponseOngDto get(String cnpj) {
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
}
