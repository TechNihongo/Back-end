package org.example.technihongo.repositories;

import org.example.technihongo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);
    User findUserByEmail(String email);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    User findByUserId(Integer userId);
    List<User> findByRole_RoleId(Integer roleId);
}
