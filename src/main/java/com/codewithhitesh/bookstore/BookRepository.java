package com.codewithhitesh.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    Book findByBookNameIgnoreCase(String bookName);
}

