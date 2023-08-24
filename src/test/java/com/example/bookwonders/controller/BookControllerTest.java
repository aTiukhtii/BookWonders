package com.example.bookwonders.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all books, expected size 2")
    public void getAllBooks_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/books"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookResponseDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get book by id 1")
    public void getBookById1_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/{bookId}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        long expectedId = 1L;
        BookResponseDto actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), BookResponseDto.class);
        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
    }

    @Test
    @WithMockUser
    @DisplayName("Get book by wrong id 999, expected not found status")
    public void getBookById999_NotOk() throws Exception {
        mockMvc.perform(get("/books/{bookId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create a kobzar book")
    public void createBook_Ok() throws Exception {
        CreateBookRequestDto createBookRequestDto = createKobzarBook();
        String request = objectMapper.writeValueAsString(createBookRequestDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/books")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookResponseDto expected = createBookResponseDto(createBookRequestDto);
        BookResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update kobzar book with id 1")
    public void updateBook_Ok() throws Exception {
        long bookId = 1;
        CreateBookRequestDto updateBookRequestDto = createKobzarBook().setIsbn("1234567890");
        updateBookRequestDto.setTitle("Updated book");
        String request = objectMapper.writeValueAsString(updateBookRequestDto);
        MvcResult mvcResult = mockMvc.perform(
                        put("/books/{id}", bookId)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookResponseDto expected = createBookResponseDto(createKobzarBook())
                .setId(bookId)
                .setIsbn("1234567890")
                .setTitle("Updated book");
        BookResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update book with wrong id 999, expected not found status")
    public void updateBook_NotOk() throws Exception {
        long bookId = 999;
        CreateBookRequestDto updateBookRequestDto = createKobzarBook().setIsbn("1234567890");
        updateBookRequestDto.setTitle("Updated book");
        String request = objectMapper.writeValueAsString(updateBookRequestDto);
        mockMvc.perform(
                        put("/books/{id}", bookId)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search book by author Taras, expected 1 book")
    public void searchBookByAuthor_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/search")
                                .param("authorsLike", "Taras"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookResponseDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Taras Shevchenko", actual.get(0).getAuthor());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search book by price from 20 to 25, expected 1 book")
    public void searchBookByPrice_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/search")
                                .param("priceFrom", "20")
                                .param("priceTo", "25"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookResponseDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(BigDecimal.valueOf(24.99), actual.get(0).getPrice());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Search book by title, expected 1 book")
    public void searchBookByTitle_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/books/search")
                                .param("titlesLike", "i"))
                .andExpect(status().isOk())
                .andReturn();
        List<BookResponseDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("It", actual.get(0).getTitle());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all-book&category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete book by id 1")
    public void deleteBookById1_Ok() throws Exception {
        mockMvc.perform(
                        delete("/books/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete book by wrong id 999, expected status not found")
    public void deleteBookByWrongId() throws Exception {
        mockMvc.perform(
                        delete("/books/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    private static CreateBookRequestDto createKobzarBook() {
        return new CreateBookRequestDto()
                .setDescription("New Book")
                .setCoverImage("kniga_name.jpg")
                .setAuthor("Taras Shevchenko")
                .setIsbn("16464565498")
                .setTitle("New Book")
                .setPrice(new BigDecimal("19.99"))
                .setCategoryIds(List.of(1L));
    }

    private static BookResponseDto createBookResponseDto(CreateBookRequestDto dto) {
        return new BookResponseDto()
                .setIsbn(dto.getIsbn())
                .setCategoryIds(dto.getCategoryIds())
                .setAuthor(dto.getAuthor())
                .setDescription(dto.getDescription())
                .setPrice(dto.getPrice())
                .setCoverImage(dto.getCoverImage())
                .setTitle(dto.getTitle())
                .setId(3L);
    }
}
