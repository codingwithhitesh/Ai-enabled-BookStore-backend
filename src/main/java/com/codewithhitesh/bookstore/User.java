package com.codewithhitesh.bookstore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity

@Table(name = "Store_Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @NotBlank(message = "User Name cannot be empty")         // Validation
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String userName;


    private enum Gender {
        MALE, FEMALE , NOT_SPECIFIED
    }
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private enum UserStatus {
        STUDENT , PROFESSIONAL , HOMEMAKER
    }
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;



    @OneToMany (mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Order> order = new ArrayList<>();

    @OneToOne (mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

    public User() {
    }

    public User(int userId, String userName, Gender gender, UserStatus userStatus) {
        this.userId = userId;
        this.userName = userName;
        this.gender = gender;
        this.userStatus = userStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
        this.order = order;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
