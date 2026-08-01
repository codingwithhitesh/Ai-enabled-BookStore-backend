package com.codewithhitesh.bookstore;

import jakarta.persistence.*;


@Entity
@Table(name = "book_orders")
public class Order {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;
    private int orderAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")    // SOP to use underscore in FK names
    private User user;

    @OneToOne
    @JoinColumn(name = "cart_Id")   // SOP to use underscore in FK names
    private Cart cart;

    public Order() {
    }

    public Order(int orderId, int orderAmount, User user, Cart cart) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.user = user;
        this.cart = cart;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
