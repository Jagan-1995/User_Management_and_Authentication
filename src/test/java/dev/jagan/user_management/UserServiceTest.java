package dev.jagan.user_management;

import dev.jagan.user_management.dtos.UserDto;
import dev.jagan.user_management.models.User;
import dev.jagan.user_management.repositories.UserRepository;
import dev.jagan.user_management.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // Use MockitoExtension to initialize mocks and inject dependencies for testing.
public class UserServiceTest {
    @Mock
    private UserRepository userRepository; // Mock the UserRepository to simulate database operations
    @Mock
    private PasswordEncoder passwordEncoder; // Mock the PasswordEncoder to simulate password encoding
    @InjectMocks
    private UserService userService; // Inject the mocked dependencies into the UserService.
    private UserDto userDto; // Define a UserDto object to simulate user input
    private User user; // Define a User object to simulate the user entity.

    @BeforeEach
    void setUp() {
        // Initialize the userDto with test data (name, email, password).
        userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test@example.com");
        userDto.setPassword("Password123!");
        // Initialize the user object that represents the user entity after creation.
        user = new User();
        user.setId(1L);
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword("encodedPassword");
    }

    @Test
    void createUser_ValidInput_Success() {
        // Simulate that the email is not already taken and that the password will be encoded.

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        // Call the createUser method and check if the result is correct
        User created = userService.createUser(userDto);
        // Assert that the created user is not null and matches the input data.
        assertNotNull(created);
        assertEquals(userDto.getName(), created.getName());
        assertEquals(userDto.getEmail(), created.getEmail());
        // Verify that the save method was called once with a User object.
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        // Mock the repository to return a user when findByEmail is called
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        // Assert that an IllegalArgumentException is thrown when creating a user with a duplicate email
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDto));

        // Verify that save was never called
        verify(userRepository, never()).save(any(User.class));
    }
}
