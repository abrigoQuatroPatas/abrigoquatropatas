package br.com.compasso.adoption.service;

import br.com.compasso.adoption.dto.response.ResponseAdoptionDto;
import br.com.compasso.adoption.dto.response.ResponseConsumerDto;
import br.com.compasso.adoption.dto.response.ResponseOngDto;
import br.com.compasso.adoption.dto.response.ResponsePetDto;
import br.com.compasso.adoption.entity.AdoptionEntity;
import br.com.compasso.adoption.exception.MessageFeignException;
import br.com.compasso.adoption.http.ConsumerClient;
import br.com.compasso.adoption.http.OngClient;
import br.com.compasso.adoption.http.PetClient;
import br.com.compasso.adoption.repository.AdoptionRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdoptionService {

    @Autowired
    private AdoptionRepository repository;
    @Autowired
    private ConsumerClient consumerClient;
    @Autowired
    private PetClient petClient;
    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private OngClient ongClient;

    public ResponseAdoptionDto post(String idConsumer, String idPet) {
        ResponseConsumerDto consumer;
        ResponsePetDto pet;
        ResponseOngDto ong;
        try {
            consumer = consumerClient.getConsumer(idConsumer);
            pet = petClient.getPet(idPet);
            ong = ongClient.getOng(pet.getOng().getCnpj());
            pet.setOng(ong);
        } catch (FeignException e) {
            throw new MessageFeignException(String.valueOf(e.status()), e.contentUTF8());
        }

        AdoptionEntity adoption = AdoptionEntity.builder()
                .petId(pet.getId())
                .consumerId(consumer.getCpf())
                .adoptionDate(LocalDate.now())
                .build();
        if (repository.existsByPetId(pet.getId())) {
            throw new ResponseStatusException(HttpStatus.OK);
        }

        ongClient.putAmountPet(pet.getOng().getCnpj(), pet.getType().toLowerCase());

        consumerClient.putStatusConsumerInProgress(idConsumer);

        repository.save(adoption);

        rabbitService.sendEmailToQueue(consumer);
        log.info("sendEmailToQueue() - Send email for: {}", consumer.getName());

        return ResponseAdoptionDto.builder()
                .id(adoption.getId())
                .consumer(consumer)
                .pet(pet)
                .adoptionDate(adoption.getAdoptionDate())
                .build();
    }

    public List<ResponseAdoptionDto> get() {
        List<AdoptionEntity> all = repository.findAll();
        List<ResponseAdoptionDto> collect = new ArrayList<>();
        try {
            all.forEach(adoption -> {
               ResponseConsumerDto consumer = consumerClient.getConsumer(adoption.getConsumerId());
               ResponsePetDto pet = petClient.getPet(adoption.getPetId());
               ResponseOngDto ong = ongClient.getOng(pet.getOng().getCnpj());
               pet.setOng(ong);

                ResponseAdoptionDto build = ResponseAdoptionDto.builder()
                        .id(adoption.getId())
                        .consumer(consumer)
                        .pet(pet)
                        .adoptionDate(adoption.getAdoptionDate())
                        .build();
                collect.add(build);
            });
        } catch (FeignException e) {
            throw new ResponseStatusException(HttpStatus.valueOf(e.status()), e.contentUTF8());
        }
        return collect;
    }

    public void putStatusConsumerApproved(String idConsumer) {
        consumerClient.putStatusConsumerApproved(idConsumer);
    }

    public void putStatusConsumerDisapproved(String idConsumer) {
        consumerClient.putStatusConsumerDisapproved(idConsumer);
    }

    public ResponseAdoptionDto get(Long id) {
        AdoptionEntity adoption = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        ResponseConsumerDto consumer = consumerClient.getConsumer(adoption.getConsumerId());
        ResponsePetDto pet = petClient.getPet(adoption.getPetId());
        ResponseOngDto ong = ongClient.getOng(pet.getOng().getCnpj());
        pet.setOng(ong);

        return ResponseAdoptionDto.builder()
                .id(adoption.getId())
                .consumer(consumer)
                .pet(pet)
                .adoptionDate(adoption.getAdoptionDate())
                .build();
    }

    public void delete(Long id) {
        AdoptionEntity adoption = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
        consumerClient.putStatusConsumerNull(adoption.getConsumerId());
        ResponsePetDto pet = petClient.getPet(adoption.getPetId());
        ongClient.putAmountPetPlus(pet.getOng().getCnpj(), pet.getType().toLowerCase());
        repository.delete(adoption);
    }
}
