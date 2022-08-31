package br.com.compasso.adoption.repository;

import br.com.compasso.adoption.entity.AdoptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionRepository extends JpaRepository<AdoptionEntity, Long> {

    boolean existsByPetId(String id);
    boolean existsByConsumerId(String id);
}
