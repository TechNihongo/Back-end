package org.example.technihongo.api;

import org.example.technihongo.core.security.JwtUtil;
import org.example.technihongo.dto.DomainRequestDTO;
import org.example.technihongo.dto.DomainResponseDTO;
import org.example.technihongo.dto.PageResponseDTO;
import org.example.technihongo.exception.ResourceNotFoundException;
import org.example.technihongo.response.ApiResponse;
import org.example.technihongo.services.interfaces.DomainService;
import org.example.technihongo.services.interfaces.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/domain")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDomain(@RequestBody DomainRequestDTO request) {
        try {
            DomainResponseDTO response = domainService.createDomain(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Domain created successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to create domain: " + e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/update/{domainId}")
    public ResponseEntity<ApiResponse> updateDomain(
            @PathVariable Integer domainId,
            @RequestBody DomainRequestDTO request) {
        try {
            DomainResponseDTO response = domainService.updateDomain(domainId, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Domain updated successfully")
                    .data(response)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Domain not found: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to update domain: " + e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/delete/{domainId}")
    public ResponseEntity<ApiResponse> deleteDomain(@PathVariable Integer domainId) {
        try {
            domainService.deleteDomain(domainId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Domain deleted successfully")
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Domain not found: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to delete domain: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllDomain(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<DomainResponseDTO> response = domainService.getAllDomains(pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No domain found!")
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Retrieved all domains successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve domains: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/parentDomain")
    public ResponseEntity<ApiResponse> getAllParentDomain(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<DomainResponseDTO> response = domainService.getAllParentDomains(pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No parent domain found!")
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Retrieved all parent domains successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve parent domains: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/childrenDomain")
    public ResponseEntity<ApiResponse> getAllChildrenDomain(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<DomainResponseDTO> response = domainService.getAllChildrenDomains(pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No child domains found!")
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Retrieved all child domains successfully")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve child domains: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("getDomain/{domainId}")
    public ResponseEntity<ApiResponse> getDomainById(@PathVariable Integer domainId) {
        try {
            DomainResponseDTO response = domainService.getDomainById(domainId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Retrieved domain successfully")
                    .data(response)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Domain not found: " + e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve domain: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/searchDomainName")
    public ResponseEntity<ApiResponse> searchDomains(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<DomainResponseDTO> response = domainService.searchName(keyword, pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No domains found matching the keyword: " + keyword)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Found domains matching the keyword")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to search domains: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/getDomainByTag")
    public ResponseEntity<ApiResponse> getDomainsByTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<DomainResponseDTO> response = domainService.getDomainsByTags(tags, pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(false)
                        .message("No domains found with the specified tags")
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Found domains with specified tags")
                    .data(response)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to get domains by tags: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/{parentId}/getChildrenTag")
    public ResponseEntity<ApiResponse> getChildDomains(
            @PathVariable("parentId") Integer parentDomainId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        try {
            PageResponseDTO<DomainResponseDTO> response = domainService.getChildDomains(parentDomainId, pageNo, pageSize, sortBy, sortDir);
            if (response.getContent().isEmpty()) {
                return ResponseEntity.ok(ApiResponse.builder()
                        .success(true)
                        .message("No child domains found for parent domain ID: " + parentDomainId)
                        .data(response)
                        .build());
            }
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .message("Retrieved child domains successfully")
                    .data(response)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .success(false)
                            .message("Failed to retrieve child domains: " + e.getMessage())
                            .build());
        }
    }
}
