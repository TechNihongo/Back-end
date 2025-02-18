package org.example.technihongo.repositories;

import org.example.technihongo.entities.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<Domain, Integer> {
    Domain findByDomainId(Integer domainId);
}
