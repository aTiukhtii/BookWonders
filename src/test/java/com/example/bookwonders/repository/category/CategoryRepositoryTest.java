package com.example.bookwonders.repository.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.bookwonders.model.Book;
import com.example.bookwonders.model.Category;
import com.example.bookwonders.repository.book.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookRepository bookRepository;
    private Category savedFictionCategory;
    private Category savedHorrorCategory;

    @BeforeEach
    void setUp() {
        savedFictionCategory = categoryRepository.save(createFictionCategory());
        Book savedKobzarBook = bookRepository.save(createKobzarBook());
        savedFictionCategory.addBook(savedKobzarBook);
        savedHorrorCategory = categoryRepository.save(createHorrorCategory());
        Book savedItBook = bookRepository.save(createItBook());
        savedHorrorCategory.addBook(savedItBook);
        savedHorrorCategory.addBook(savedKobzarBook);
    }

    @Test
    @DisplayName("Get all books by fiction category")
    void getBooksByCategoriesId_WhereIdIs1_ExpectedOneBook() {
        List<Book> actual = categoryRepository.getBooksByCategoriesId(savedFictionCategory.getId());
        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    @DisplayName("Get all books by horror category")
    void getBooksByCategoriesId_WhereIdIs2_ExpectedTwoBook() {
        List<Book> actual = categoryRepository.getBooksByCategoriesId(savedHorrorCategory.getId());
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("Get empty list of books by non-existent category")
    void getBooksByCategoriesId_WhereIdIs999_NotOk() {
        List<Book> actual = categoryRepository.getBooksByCategoriesId(999L);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    private static Category createFictionCategory() {
        Category category = new Category();
        category.setName("Fiction");
        category.setDescription("Fiction books");
        return category;
    }

    private static Book createKobzarBook() {
        Book book = new Book();
        book.setDescription("Kobzar Book");
        book.setCoverImage("kniga_kobzar.jpg");
        book.setAuthor("Taras Shevchenko");
        book.setIsbn("1234567890");
        book.setTitle("Kobzar");
        book.setPrice(new BigDecimal("19.99"));
        return book;
    }

    private static Book createItBook() {
        Book book = new Book();
        book.setDescription("Horror book");
        book.setCoverImage("kniga_It.jpg");
        book.setAuthor("Stephen King");
        book.setIsbn("9876543210");
        book.setTitle("It");
        book.setPrice(new BigDecimal("24.99"));
        return book;
    }

    private static Category createHorrorCategory() {
        Category category = new Category();
        category.setName("Fiction");
        category.setDescription("Fiction books");
        return category;
    }
}
