package com.codewithhitesh.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

}

/*
{
    "bookName": "Finance magazine",
    "writerName": "The nature Team",
    "sellingPrice": "340",
    "category": "Science",
    "stockQuantity": 100

}
{
    "userName": "Hitesh_Jaipur",
    "gender" : "MALE",
    "userStatus": "PROFESSIONAL"
}

http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:bookstore

http://localhost:8080/api/H2/OnlineBookStore/India/addUser  Postman



 */
