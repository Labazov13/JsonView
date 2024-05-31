package com.example.JsonView.services;

import com.example.JsonView.dto.UserDTO;
import com.example.JsonView.entities.User;
import com.example.JsonView.exceptions.UserNotFoundException;
import com.example.JsonView.repositories.UserRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() ->new UserNotFoundException("Cannot find user with ID: " + id));
    }
    public User createUser(UserDTO dto){
        User user = new User(dto.name(), dto.email(), new ArrayList<>());
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Cannot find user with ID: " + userId));
        userRepository.delete(user);
    }
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public User editUser(Long id, UserDTO dto){
        User userFromDB = userRepository.findById(id)
                .orElseThrow(() ->new UserNotFoundException("Cannot find user with ID: " + id));
        userFromDB.setName(dto.name());
        userFromDB.setEmail(dto.email());
        userRepository.save(userFromDB);
        return userFromDB;
    }
}
