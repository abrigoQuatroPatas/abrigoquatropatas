package br.com.compasso.voluntary.repository;

import br.com.compasso.voluntary.entity.VoluntaryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoluntaryRepository extends MongoRepository<VoluntaryEntity, String> {

    List<VoluntaryEntity> findByOngId(String cnpj);

}
