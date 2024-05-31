package com.example.JsonView.controllers;

import com.example.JsonView.dto.UserDTO;
import com.example.JsonView.entities.Order;
import com.example.JsonView.entities.User;
import com.example.JsonView.services.OrderService;
import com.example.JsonView.services.UserService;
import com.example.JsonView.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.UserSummary.class)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.UserDetails.class)
    public User getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @Validated
    @PostMapping(value = "/new")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.createUser(dto));
    }

    @PostMapping(value = "/{userId}/order/new")
    public User createOrder(@PathVariable(name = "userId") Long userId) {
        User user = userService.getUserById(userId);
        orderService.createOrder(user);
        return user;
    }
    @PutMapping(value = "/{userId}/order/pay/{orderId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public User payForTheOrder(@PathVariable(name = "userId") Long userId, @PathVariable(name = "orderId") Long orderId){
        User user = userService.getUserById(userId);
        Order order = orderService.findOrder(orderId);
        return orderService.editOrder(user, order);
    }

    @DeleteMapping(value = "/delete/{userId}")
    public void deleteUser(@PathVariable(name = "userId") Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping(value = "/edit/{userId}")
    public User editUser(@PathVariable(name = "userId") Long userId, @Valid @RequestBody UserDTO dto) {
        return userService.editUser(userId, dto);
    }
}
