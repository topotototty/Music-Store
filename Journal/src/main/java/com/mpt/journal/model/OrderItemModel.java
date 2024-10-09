package com.mpt.journal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @NotNull(message = "Идентификатор заказа обязателен")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderModel order;

    @NotNull(message = "Идентификатор альбома обязателен")
    @ManyToOne
    @JoinColumn(name = "album_id", nullable = false)
    private AlbumModel album;

    @Min(value = 1, message = "Количество должно быть минимум 1")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}