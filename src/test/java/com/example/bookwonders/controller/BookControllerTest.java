package com.example.bookwonders.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookwonders.dto.book.BookResponseDto;
import com.example.bookwonders.dto.book.CreateBookRequestDto;
import com.example.bookwonders.dto.book.UpdateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
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
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books&categories/add-books-and-categories.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource source) {
        teardown(source);
    }

    @SneakyThrows
    private static void teardown(DataSource source) {
        try (Connection connection = source.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/books&categories"
                            + "/delete-books-and-categories.sql"));
        }
    }

    @Test
    @WithMockUser
    @DisplayName("Get all books, expected size 2")
    public void getAllBooks_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookResponseDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser
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
    @DisplayName("Create a kobzar book")
    @Sql(scripts = "classpath:database/books&categories/delete-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
    @DisplayName("Update kobzar book with id 1")
    public void updateBook_Ok() throws Exception {
        long bookId = 1;
        UpdateBookRequestDto updateBookRequestDto = createUpdateRequest();
        updateBookRequestDto.setTitle("Updated book");
        String request = objectMapper.writeValueAsString(updateBookRequestDto);
        MvcResult mvcResult = mockMvc.perform(
                        put("/books/{id}", bookId)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookResponseDto expected = createBookResponseDto(createKobzarBook()).setId(bookId).setIsbn("1234567890");
        expected.setTitle("Updated book");
        BookResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookResponseDto.class).setCategoryIds(List.of(1L));
        assertNotNull(actual);
        assertEquals(expected, actual);
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

    private static UpdateBookRequestDto createUpdateRequest() {
        return new UpdateBookRequestDto()
                .setDescription("New Book")
                .setCoverImage("kniga_name.jpg")
                .setAuthor("Taras Shevchenko")
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
