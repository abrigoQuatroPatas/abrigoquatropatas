package br.com.compasso.pet.repository;

import br.com.compasso.pet.entity.PetEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends MongoRepository<PetEntity, String> {

    List<PetEntity> findByOngId(String cnpj);

}
