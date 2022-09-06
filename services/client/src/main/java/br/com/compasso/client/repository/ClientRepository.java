package br.com.compasso.client.repository;

import br.com.compasso.client.entity.ClientEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<ClientEntity, String> {
}
