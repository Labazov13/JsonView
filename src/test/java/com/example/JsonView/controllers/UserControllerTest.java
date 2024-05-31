package com.example.JsonView.controllers;

import com.example.JsonView.dto.UserDTO;
import com.example.JsonView.entities.User;
import com.example.JsonView.exceptions.UserNotFoundException;
import com.example.JsonView.services.GenerateListProducts;
import com.example.JsonView.services.OrderService;
import com.example.JsonView.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;
    @MockBean
    GenerateListProducts generateListProducts;

    @Test
    void getAllUsers_ReturnedListUsers() throws Exception {
        List<User> users = new ArrayList<>();
        given(userService.getAllUsers()).willReturn(users);
        mockMvc.perform(get("/api/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void getUserById_ReturnedCorrectUserWithStauts_OK() throws Exception {
        Long id = 1L;
        given(userService.getUserById(id))
                .willReturn(new User(1L, "Petr", "email@gmail.com", new ArrayList<>()));
        String expectedJson = "{\"id\":1,\"name\":\"Petr\",\"email\":\"email@gmail.com\",\"orders\":[]}";
        mockMvc.perform(get("/api/users/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void getUserById_WithIncorrectUserIdReturnedStatus_BAD_REQUEST() throws Exception {
        Long id = 2L;
        given(userService.getUserById(id)).willThrow(new UserNotFoundException("Cannot find user with ID: " + id));
        String message = "Cannot find user with ID: " + id;
        mockMvc.perform(get("/api/users/{id}", id).contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string(message));
    }

    @Test
    void createUserWithValidData_ReturnedNewUserWithStatus_OK() throws Exception {
        UserDTO userDTO = new UserDTO("Petr", "petr@mail.com");
        User user = new User(1L, userDTO.name(), userDTO.email(), Collections.emptyList());
        given(userService.createUser(userDTO)).willReturn(user);
        String expectedJson = "{" +
                "               \"id\":1," +
                "               \"name\":\"Petr\"," +
                "               \"email\":\"petr@mail.com\"," +
                "               \"orders\":[]" +
                "               }";
        mockMvc.perform(post("/api/users/new").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void createUserWithInvalidName_ReturnedErrorWithStatus_BAD_REQUEST() throws Exception {
        UserDTO userDTO = new UserDTO(null, "petr@mail.com");
        String userToJSON = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/api/users/new").contentType(MediaType.APPLICATION_JSON)
                        .content(userToJSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Field not can be empty"));

    }

    @Test
    void createOrder_ReturnedUserWithNewOrderWithStatus_OK() throws Exception {
        Long id = 1L;
        User user = new User(1L, "Petr", "email@gmail.com", new ArrayList<>());
        given(userService.getUserById(id))
                .willReturn(user);
        orderService.createOrder(user);
        verify(orderService).createOrder(user);
        mockMvc.perform(post("/api/users/{userId}/order/new", id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void payForTheOrder() throws Exception {
        Long userId = 1L;
        Long orderId = 1L;
        mockMvc.perform(put("/api/users/" + userId + "/order/pay/" + orderId))
                .andExpect(status().isOk());

    }

}