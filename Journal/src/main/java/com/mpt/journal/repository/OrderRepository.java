package com.mpt.journal.repository;

import com.mpt.journal.model.OrderModel;
import com.mpt.journal.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderModel, Long> {
    List<OrderModel> findByUser(UserModel user);
    List<OrderModel> findByUserUserId(Long userId);
}