package com.example.JsonView.repositories;

import com.example.JsonView.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
