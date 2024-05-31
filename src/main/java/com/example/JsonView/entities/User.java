package com.example.JsonView.entities;

import com.example.JsonView.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "users_data")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSummary.class)
    private Long id;
    @Column(name = "user_name", nullable = false)
    @JsonView(Views.UserSummary.class)
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    @JsonView(Views.UserSummary.class)
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @JsonView(Views.UserDetails.class)
    private List<Order> orders;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email, List<Order> orders) {
        this.name = name;
        this.email = email;
        this.orders = orders;
    }
}
