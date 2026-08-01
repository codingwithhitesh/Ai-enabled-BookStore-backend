package com.codewithhitesh.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    Optional<CartItem> findItemByCartAndBook (Cart cart, Book book);
}
