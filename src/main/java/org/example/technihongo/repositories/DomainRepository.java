package org.example.technihongo.repositories;

import org.example.technihongo.entities.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Integer> {
    List<Domain> findByParentDomain(Domain parentDomainId);

    List<Domain> findByNameContainingIgnoreCase(String keyword);

    List<Domain> findByTagIn(List<String> tags);

    Domain findByDomainId(Integer domainId);
}


