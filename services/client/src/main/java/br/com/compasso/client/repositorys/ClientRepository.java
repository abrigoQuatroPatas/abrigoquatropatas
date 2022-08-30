package br.com.compasso.client.repositorys;

<<<<<<< HEAD
import br.com.compasso.client.entities.ClientEntity;
=======
import br.com.compasso.client.entitys.ClientEntity;
>>>>>>> origin/master
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<ClientEntity, String> {
}
