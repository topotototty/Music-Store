package com.mpt.journal.service;

import com.mpt.journal.model.AlbumModel;
import com.mpt.journal.model.OrderModel;
import com.mpt.journal.model.OrderStatus;
import com.mpt.journal.model.UserModel;
import com.mpt.journal.repository.AlbumRepository;
import com.mpt.journal.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Transactional
    public OrderModel createOrder(OrderModel order, List<Long> albumIds) {
        // Установить начальные параметры для заказа
        order.setStatus(OrderStatus.NEW);
        order.setTotalPrice(BigDecimal.ZERO);

        // Сохранить заказ
        OrderModel savedOrder = orderRepository.save(order);

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Обработка каждого альбома в заказе
        for (Long albumId : albumIds) {
            AlbumModel album = albumRepository.findById(albumId)
                    .orElseThrow(() -> new RuntimeException("Альбом с ID " + albumId + " не найден"));

            // Добавляем альбом в заказ
            savedOrder.getAlbums().add(album);

            // Рассчитываем итоговую цену
            totalPrice = totalPrice.add(album.getPrice());
        }

        // Обновляем итоговую цену заказа
        savedOrder.setTotalPrice(totalPrice);

        // Сохраняем обновленный заказ с добавленными альбомами и итоговой ценой
        return orderRepository.save(savedOrder);
    }

    public OrderModel getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Заказ с ID " + orderId + " не найден"));
    }

    public List<OrderModel> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteOrder(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Заказ с ID " + orderId + " не найден");
        }
    }

    public List<OrderModel> getOrdersByUser(UserModel user) {
        return orderRepository.findByUser(user);
    }

    public OrderModel saveOrder(OrderModel order) {
        return orderRepository.save(order);
    }


    public List<OrderModel> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserUserId(userId);
    }
}
