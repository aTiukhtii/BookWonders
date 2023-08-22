package com.example.bookwonders.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookwonders.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookwonders.dto.category.CategoryResponseDto;
import com.example.bookwonders.dto.category.CreateCategoryDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class CategoryControllerTest {
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
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all categories, expected size 2")
    void getAllCategories_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/categories"))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryResponseDto> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create category ")
    void createCategory_Ok() throws Exception {
        CreateCategoryDto createCategoryDto = new CreateCategoryDto()
                .setName("Created category")
                .setDescription("Created category description");
        String request = objectMapper.writeValueAsString(createCategoryDto);
        MvcResult mvcResult = mockMvc.perform(
                        post("/categories")
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryResponseDto expected = new CategoryResponseDto()
                .setDescription(createCategoryDto.getDescription())
                .setName(createCategoryDto.getName())
                .setId(3L);
        CategoryResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), CategoryResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update fiction category with id 1")
    void updateCategory_Ok() throws Exception {
        long categoryId = 1;
        CreateCategoryDto updateBookRequestDto = new CreateCategoryDto()
                .setName("Updated category")
                .setDescription("Updated category description");;
        String request = objectMapper.writeValueAsString(updateBookRequestDto);
        MvcResult mvcResult = mockMvc.perform(
                        put("/categories/{id}", categoryId)
                                .content(request)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(categoryId)
                .setName(updateBookRequestDto.getName())
                .setDescription(updateBookRequestDto.getDescription());
        CategoryResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), CategoryResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update category with wrong id 999, expected status not found")
    void updateCategory_NotOk() throws Exception {
        long categoryId = 999;
        CreateCategoryDto updateBookRequestDto = new CreateCategoryDto()
                .setName("Updated category")
                .setDescription("Updated category description");
        String request = objectMapper.writeValueAsString(updateBookRequestDto);
        mockMvc.perform(put("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get category by id 1")
    void getCategoryById_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/{bookId}", 1L))
                .andExpect(status().isOk())
                .andReturn();
        long expectedId = 1L;
        CategoryResponseDto actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), CategoryResponseDto.class);
        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get category by wrong id 999, expected status not found")
    void getCategoryById_NotOk() throws Exception {
        mockMvc.perform(
                        get("/categories/{bookId}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get books by category id 2, expected size 2")
    void getBooksByCategoryId_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        get("/categories/{id}/books", 2L))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(mvcResult
                .getResponse().getContentAsString(), new TypeReference<>() {});
        long expectedSize = 2;
        assertNotNull(actual);
        assertEquals(expectedSize, actual.size());
    }

    @Test
    @WithMockUser
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get books by wrong category id 999, expected status not found")
    void getBooksByCategoryId_NotOk() throws Exception {
        mockMvc.perform(get("/categories/{id}/books", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete category with id 1")
    void deleteCategoryById_Ok() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:database/books&categories/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books&categories/delete-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Delete category with wrong id 999, expected status not found")
    void deleteCategoryById_NotOk() throws Exception {
        mockMvc.perform(delete("/categories/{id}", 999L))
                .andExpect(status().isNotFound());
    }
}