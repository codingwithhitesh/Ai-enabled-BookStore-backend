package com.codewithhitesh.bookstore;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Jaipur-BookStore-online/ai")
public class CartOrderController {

    private UserBookService userBookService;
    private CartOrderService cartOrderService;

    public CartOrderController(UserBookService userBookService, CartOrderService cartOrderService) {
        this.userBookService = userBookService;
        this.cartOrderService = cartOrderService;
    }

    @PostMapping("/addUser")
    public String createUserInDb(@Valid @RequestBody User user) {

        userBookService.createNewUser(user);
        System.out.println(user);
        return "User added";
    }
    @PostMapping("/updateUser/{userId}")
    public String updateUserInDb(@PathVariable int userId,@Valid @RequestBody User user) {

        userBookService.updateUser(userId,user);
        return "User updated";
    }

    @DeleteMapping ("/deleteUser/{userId}")
    public String deleteUserInDb(@PathVariable int userId) {

        userBookService.deleteUser(userId);
        return "User deleted";
    }

    @PostMapping("/addBook")
    public String createBookInDb(@Valid @RequestBody Book book) {

        userBookService.registerNewBook(book);
        System.out.println(book);
        return "Book added";

    }

    @PostMapping("/manageCart/{bookId}/{userId}/{quantity}")
    public String manageCart(@PathVariable int bookId, @PathVariable int userId,@Valid @PathVariable int quantity){
        // MATCHING THE SERVICE: bookId first, then userId
        cartOrderService.addToCart(bookId, userId, quantity);
        return "item / items successfully added to the cart";

    }

    @PostMapping("/placeYourOrder/{cartId}")
    public String placeYourOrder (@PathVariable int cartId){

        cartOrderService.placeOrder(cartId);
        return "Order placed as successfully";

    }

    @GetMapping ("/getUserList")                  // GET /getUserList?page=0&size=2&sort=bookName
    public Page<User> getAllUsers(Pageable pageable) {
        return userBookService.getAllUser(pageable);
    }

    @GetMapping ("/getBookList")                  // GET /getBookList?page=0&size=2&sort=bookName
    public Page<Book> getAllBooks(Pageable pageable) {
        return userBookService.getAllBooks(pageable);
    }


}


/*
@PostMapping("/addUser")    FOR NEXT PHASE
public Map<String, Object> createUserInDb(@RequestBody User user) {

    User savedUser = userBookService.createNewUser(user);

    return Map.of(
            "message", "User added successfully",
            "user", savedUser
    );
}
 */


