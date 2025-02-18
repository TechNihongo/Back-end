package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.DomainRequestDTO;
import org.example.technihongo.dto.DomainResponseDTO;

import java.util.List;

public interface DomainService {
    DomainResponseDTO createDomain(DomainRequestDTO request);
    DomainResponseDTO updateDomain(Integer domainId, DomainRequestDTO request);
    void deleteDomain(Integer domainId);
    List<DomainResponseDTO> getAllDomains();
    DomainResponseDTO getDomainById(Integer domainId);

    List<DomainResponseDTO> searchName(String keyword);

//    boolean hasCourses(Integer domainId);
    List<DomainResponseDTO> getDomainsByTags(List<String> tags);

    List<DomainResponseDTO> getChildDomains(Integer parentDomainId);

}
