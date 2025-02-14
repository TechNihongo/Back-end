package org.example.technihongo.repositories;

import org.example.technihongo.entities.Role;
import org.example.technihongo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
