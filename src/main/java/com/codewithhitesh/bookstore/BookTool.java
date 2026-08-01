package com.codewithhitesh.bookstore;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class BookTool {

    private final UserBookService userBookService;

    public BookTool(UserBookService userBookService) {
        this.userBookService = userBookService;
    }


    // Stock calculator
    @Tool(description = "Checks the stock quantity of a book using its name")
    public String getBookAvailability(String bookName) {
        return userBookService.checkBookStock(bookName);
    }

    // Price calculator
    @Tool(description = "Checks the selling price of a book using its name")
    public String getBookPrice(String bookName) {
        return userBookService.checkBookPrice(bookName);
    }
}