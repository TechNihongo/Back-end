package org.example.technihongo.services.serviceimplements;

import lombok.RequiredArgsConstructor;
import org.example.technihongo.dto.LoginResponseDTO;
import org.example.technihongo.entities.User;
import org.example.technihongo.repositories.UserRepository;
import org.example.technihongo.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public LoginResponseDTO login(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("User are not exists"));

        if (!user.getPassword().equals(password)) {
            throw new Exception("Invalid password");
        }
    
        return new LoginResponseDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole().getRoleName(),
                true,
                "Login Successfully"
        );
    }

    @Override
    public List<User> userList() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean userNameExists(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }


    public void validateEmail(String email) throws Exception {
        if (emailExists(email)) {
            throw new Exception("Email '" + email + "' is already registered");
        }
    }



}
