package com.codewithhitesh.bookstore;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)           // auto ID
    private int bookId;


    @NotBlank(message = "Book Name cannot be empty")                 // Validation
    @Size(min = 3, max = 50, message = "Book name must be between 3 and 50 characters")
    private String bookName;

    @NotBlank(message = "Writer Name cannot be empty")
    @Size(min = 3, max = 20, message = "Writer name must be between 3 and 20 characters")
    private String writerName;

    private int sellingPrice;
    private int stockQuantity;


    private enum Category {Science, Finance, Arts, Kids, Novel}
    @Enumerated(EnumType.STRING)
    private Category category;




    @OneToMany (mappedBy = "book")
    @JsonIgnore
    private List<CartItem> cartItem = new ArrayList<>();



    public Book(int bookId, String bookName, String writerName, int sellingPrice, int stockQuantity,
                Category category, List<CartItem> cartItem) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.writerName = writerName;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.category = category;
        //this.cartItem = cartItem;
    }

    public Book() {
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }
}
