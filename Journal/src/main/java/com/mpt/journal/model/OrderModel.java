package com.mpt.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @NotBlank(message = "Номер заказа не может быть пустым")
    @Size(max = 5, message = "Номер заказа должен быть не более 5 символов")
    @Column(name = "order_number", nullable = false, length = 5)
    private String orderNumber;

    @NotNull(message = "Идентификатор пользователя обязателен")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @DecimalMin(value = "0.0", inclusive = false, message = "Итоговая цена должна быть больше 0")
    @Digits(integer = 10, fraction = 2, message = "Цена должна быть в формате DECIMAL(10,2)")
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private OrderStatus status;

    @ManyToMany
    @JoinTable(
            name = "order_albums", // имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "album_id")
    )
    private List<AlbumModel> albums;  // Список альбомов в заказе
}
