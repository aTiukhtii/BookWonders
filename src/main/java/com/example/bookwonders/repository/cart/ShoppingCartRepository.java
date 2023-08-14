package com.example.bookwonders.repository.cart;

import com.example.bookwonders.model.ShoppingCart;
import com.example.bookwonders.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, User> {
}
