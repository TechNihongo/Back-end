package org.example.technihongo.services.interfaces;


import org.example.technihongo.dto.LoginResponseDTO;
import org.example.technihongo.dto.UserLogin;
import org.example.technihongo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserService {
    LoginResponseDTO login(String email, String password) throws Exception;

    List<User> userList();


    Optional<User> findUserByEmail(String email);

    boolean userNameExists(String userName);

    boolean emailExists(String email);
}
