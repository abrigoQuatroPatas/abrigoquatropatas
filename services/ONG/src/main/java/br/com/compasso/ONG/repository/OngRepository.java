package br.com.compasso.ONG.repository;

import br.com.compasso.ONG.entity.OngEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngRepository extends MongoRepository<OngEntity, String> {

}
