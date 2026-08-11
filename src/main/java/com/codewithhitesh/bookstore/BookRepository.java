package com.codewithhitesh.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    //Book findByBookNameIgnoreCase(String bookName);

    List <Book> findByBookNameContainingIgnoreCase(String bookName);
}

