package com.example.bookwonders.repository.order;

import com.example.bookwonders.model.Order;
import com.example.bookwonders.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getAllByUser(Pageable pageable, User user);
}
