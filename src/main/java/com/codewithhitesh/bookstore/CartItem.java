package com.codewithhitesh.bookstore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;


@Entity
public class CartItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ItemId;

    @ManyToOne
    @JoinColumn (name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn (name = "cart_id")
    @JsonIgnore
    private Cart cart;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    public CartItem(int itemId, Book book, Cart cart, int quantity) {
        ItemId = itemId;
        this.book = book;
        this.cart = cart;
        this.quantity = quantity;
    }

    public CartItem() {

    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
