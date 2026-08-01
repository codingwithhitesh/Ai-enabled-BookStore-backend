package com.codewithhitesh.bookstore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity

public class Cart {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;
    private int cartValue;



    @OneToMany (mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItem = new ArrayList<>();


    @OneToOne
    @JoinColumn (name = "userId")
    @JsonIgnore
    private User user;

    public Cart(int cartId, int cartValue, List<CartItem> cartItem, User user) {
        this.cartId = cartId;
        this.cartValue = cartValue;
        this.cartItem = cartItem;
        this.user = user;
    }

    public Cart() {

    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCartValue() {
        return cartValue;
    }

    public void setCartValue(int cartValue) {
        this.cartValue = cartValue;
    }

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
