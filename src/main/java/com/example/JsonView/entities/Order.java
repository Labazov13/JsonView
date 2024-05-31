package com.example.JsonView.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Table(name = "orders_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<Product> products;
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    private String status;

    public Order(List<Product> products, BigDecimal totalAmount, String status) {
        this.products = products;
        this.totalAmount = totalAmount;
        this.status = status;
    }

}
