package com.sayan.article_platform.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sayan.article_platform.controller.AuthController;
import com.sayan.article_platform.dto.request.LoginRequest;
import com.sayan.article_platform.dto.request.RegisterRequest;
import com.sayan.article_platform.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldHandleResourceNotFoundException() throws Exception {

        LoginRequest request = new LoginRequest("sayan@test.com", "password123");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(
                        new ResourceNotFoundException("User not found")
                );

        mockMvc.perform(
                        post("/api/auth/login")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isNotFound())

                .andExpect(content()
                        .string("User not found"));
    }

    @Test
    void shouldHandleGenericException() throws Exception {

        LoginRequest request = new LoginRequest("sayan@test.com", "password123");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(
                        new RuntimeException("Something went wrong")
                );

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isInternalServerError())

                .andExpect(content()
                        .string("Something went wrong"));
    }

    @Test
    void shouldHandleValidationException() throws Exception {

        LoginRequest request = new LoginRequest("", "");

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleDuplicateException() throws Exception {

        RegisterRequest request = new RegisterRequest("sayan", "test@test.com", "password");

        doThrow(
                new DataIntegrityViolationException(
                        "Duplicate entry"
                )
        ).when(authService)
                .register(any(RegisterRequest.class));

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isConflict())

                .andExpect(jsonPath("$.error")
                        .value("DUPLICATE"))

                .andExpect(jsonPath("$.message")
                        .value("Data integrity violation"));
    }
}