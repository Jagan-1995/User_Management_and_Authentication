package dev.jagan.user_management.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.jagan.user_management.config.RateLimitingConfig;
import dev.jagan.user_management.controllers.UserController;
import dev.jagan.user_management.dtos.UserDto;
import dev.jagan.user_management.models.User;
import dev.jagan.user_management.security.JwtTokenProvider;
import dev.jagan.user_management.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private RateLimitingConfig rateLimitingConfig;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Create user with valid input - Returns Created")
    @WithMockUser(roles = "ADMIN")
    public void createUser_ValidInput_ReturnsCreated() throws Exception {
        // Arrange
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPassword("Password123!");

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setName("Test User");
        createdUser.setEmail("test@example.com");

        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        // Act & Assert
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }
}