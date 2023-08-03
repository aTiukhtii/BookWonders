package com.example.bookwonders.service;

import com.example.bookwonders.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book product);

    List<Book> findAll();
}
