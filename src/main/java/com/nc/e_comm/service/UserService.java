package com.nc.e_comm.service;

import com.nc.e_comm.model.User;
import com.nc.e_comm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(User user) {
        User newUser = userRepository.save(user);
        System.out.println("User registered: " + newUser);
        return newUser;
    }

    public User loginUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // If you are storing raw passwords (not recommended)
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
