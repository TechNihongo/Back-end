package org.example.technihongo.entities;


import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Domain")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "domain_id")
    private Integer domainId;
    
    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "parent_domain_id")
    private Domain parentDomain;

    @OneToMany(mappedBy = "parentDomain", cascade = CascadeType.ALL)
    private List<Domain> subDomains;

    
    

}
