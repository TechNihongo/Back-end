package org.example.technihongo.services.interfaces;

import org.example.technihongo.dto.DomainRequestDTO;
import org.example.technihongo.dto.DomainResponseDTO;
import org.example.technihongo.dto.PageResponseDTO;

import java.util.List;

public interface DomainService {
    DomainResponseDTO createDomain(DomainRequestDTO request);
    DomainResponseDTO updateDomain(Integer domainId, DomainRequestDTO request);
    void deleteDomain(Integer domainId);
    PageResponseDTO<DomainResponseDTO> getAllDomains(int pageNo, int pageSize, String sortBy, String sortDir);

    PageResponseDTO<DomainResponseDTO> getAllParentDomains(int pageNo, int pageSize, String sortBy, String sortDir);
    PageResponseDTO<DomainResponseDTO> getAllChildrenDomains(int pageNo, int pageSize, String sortBy, String sortDir);
    DomainResponseDTO getDomainById(Integer domainId);

    PageResponseDTO<DomainResponseDTO> searchName(String keyword, int pageNo, int pageSize, String sortBy, String sortDir);

    PageResponseDTO<DomainResponseDTO> getDomainsByTags(List<String> tags, int pageNo, int pageSize, String sortBy, String sortDir);

    PageResponseDTO<DomainResponseDTO> getChildDomains(Integer parentDomainId, int pageNo, int pageSize, String sortBy, String sortDir);
}