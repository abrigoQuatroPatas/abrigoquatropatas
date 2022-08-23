package br.com.compasso.ONG.service;

import br.com.compasso.ONG.dto.request.RequestOngDto;
import br.com.compasso.ONG.dto.response.ResponseOngDto;
import br.com.compasso.ONG.entity.OngEntity;
import br.com.compasso.ONG.repository.OngRepository;
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

    public ResponseOngDto post(RequestOngDto ong) {
        OngEntity ongEntity = modelMapper.map(ong, OngEntity.class);
        if (ongRepository.existsById(ong.getCnpj())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }
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
