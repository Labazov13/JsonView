package com.example.JsonView.services;

import com.example.JsonView.entities.Order;
import com.example.JsonView.entities.Product;
import com.example.JsonView.entities.User;
import com.example.JsonView.exceptions.OrderNotFoundException;
import com.example.JsonView.repositories.OrderRepository;
import com.example.JsonView.repositories.UserRepository;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GenerateListProducts generateListProducts;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        GenerateListProducts generateListProducts) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.generateListProducts = generateListProducts;
    }

    public void createOrder(User user) {
        List<Product> generatedProducts = generateListProducts.generateListOfProducts();
        BigDecimal totalCost = getTotalCost(generatedProducts);
        Order order = new Order(generatedProducts, totalCost, "CREATED");
        user.getOrders().add(order);
        orderRepository.save(order);
        userRepository.save(user);
    }

    public BigDecimal getTotalCost(List<Product> products){
        return products.stream().map(Product::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Order findOrder(Long orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Cannot find user with ID: " + orderId));
    }
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public User editOrder(User user, Order order){
        order.setStatus("PAID");
        orderRepository.save(order);
        return userRepository.save(user);
    }
}
