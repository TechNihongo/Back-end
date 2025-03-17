package org.example.technihongo.repositories;

import org.example.technihongo.entities.Domain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Integer> {
    Page<Domain> findByParentDomain(Domain parentDomainId, Pageable pageable);
    Page<Domain> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Domain> findByTagIn(List<String> tags, Pageable pageable);
    Domain findByDomainId(Integer domainId);
    Page<Domain> findByParentDomainIsNull(Pageable pageable);
    Page<Domain> findByParentDomainIsNotNull(Pageable pageable);
}