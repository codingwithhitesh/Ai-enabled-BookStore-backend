package com.codewithhitesh.bookstore;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBookService {

    private  UserRepository userRepository;
    private BookRepository bookRepository;

    public UserBookService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }


    public User createNewUser(User user) {

        return userRepository.save(user);
    }

    public User updateUser(int userId, User updatedUser) {

        User existingUser =  userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(
                        "User with ID " + userId + " not found"
                ));

        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setUserStatus(updatedUser.getUserStatus());
        existingUser.setGender(updatedUser.getGender());
        return userRepository.save(existingUser);

    }

    public  String deleteUser(int userId) {

        User user =  userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(
                        "User with ID " + userId + " not found"
                ));
        userRepository.delete(user);
        return "User deleted";
    }

    public Book registerNewBook(Book book) {

        return bookRepository.save(book);
    }


    public User getUserById(int userId){
        return userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(
                        "User with ID " + userId + " not found"
                ));

    }

    public Page<User> getAllUser(Pageable pageable){
        return userRepository.findAll(pageable);

    }

    public Book getBookById(int bookId){
        return bookRepository.findById(bookId).orElseThrow(() ->
                new BookNotFoundException(
                        "Book with ID " + bookId + " not found"
                ));

    }
    public Page<Book> getAllBooks(Pageable pageable){                   // Learning
        return bookRepository.findAll(pageable);

    }


    // Method to be used by AI LLM for Stock Enquiry
    public String checkBookStock(String bookName){
        List <Book> books = bookRepository.findByBookNameContainingIgnoreCase(bookName);
        if (books == null) {
            return "Sorry, the book '" + bookName + "' was not found.";
        }
        Book book = books.get(0); // Extracts the first Book object from the list
        return "Book: " + book.getBookName()
                + ", Available Stock: "
                + book.getStockQuantity();

    }


    // Method to be used by AI LLM for Price Enquiry
    public String checkBookPrice(String bookName) {
        List<Book> books = bookRepository.findByBookNameContainingIgnoreCase(bookName);
        if (books == null) {
            return "Sorry, the book '" + bookName + "' was not found.";
        }
        Book book = books.get(0); // Extracts the first Book object from the list
        return "Book: " + book.getBookName()
                + "has a selling price of -  "
                + book.getSellingPrice();
    }
}
