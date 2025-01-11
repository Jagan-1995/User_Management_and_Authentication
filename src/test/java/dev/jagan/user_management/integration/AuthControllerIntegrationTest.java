package dev.jagan.user_management.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jagan.user_management.dtos.LoginRequest;
import dev.jagan.user_management.dtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest  // Load the full Spring application context for integration tests.
@AutoConfigureMockMvc  // Automatically configure MockMvc to test the controller without starting the actual server.
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc to perform HTTP requests in the tests.

    @Autowired
    private ObjectMapper objectMapper;  // ObjectMapper to convert java objects to JSON and vice versa.

    @Test
    void registerAndLogin_Success() throws Exception {
        // Create a new UserDto for testing registration with name, email, and password
        UserDto userDto = new UserDto();
        userDto.setName("Integration Test");
        userDto.setEmail("integration@test.com");
        userDto.setPassword("Test123!");

        // Send a POST request to the registration endpoint with the UserDto.
        // This tests user registration.
        // Register
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON) // Specify that the request body is JSON.
                        .content(objectMapper.writeValueAsString(userDto))) // Convert the UserDto to JSON.
                .andExpect(status().isOk())  // Assert that the status is 200 OK.
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));  // Assert the email in the response matches the input.
        // Create a LoginRequest for logging in with the email and password of the registered user
        // Login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(userDto.getEmail());
        loginRequest.setPassword(userDto.getPassword());
        // Send a POST request to the login endpoint with the LoginRequest.
        // This tests user login.
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON) // Specify that the request body is JSON.
                        .content(objectMapper.writeValueAsString(loginRequest))) // Convert the LoginRequest to JSON.
                .andExpect(status().isOk())  // Assert that the status is 200 OK.
                .andExpect(jsonPath("$.token").exists()); // Assert that the response contains a token, indicating successful login.
    }
}