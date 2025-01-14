package org.example.technihongo.entities;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "[User]")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;

    @Column(name="username", unique = true)
    private String userName;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "uid")
    private String uid;

    @Column(name = "is_active")
    private boolean isActive = true;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "last_login")
    private LocalDate lastLogin;

    @JsonIgnore
    @Column(name="role_id")
    private Integer roleId;

    @Column(name = "profile_img")
    private String profileImg;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Student student;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;

}
