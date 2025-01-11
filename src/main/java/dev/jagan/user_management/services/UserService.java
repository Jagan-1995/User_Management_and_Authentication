package dev.jagan.user_management.services;

import dev.jagan.user_management.dtos.UserDto;
import dev.jagan.user_management.models.User;
import dev.jagan.user_management.repositories.UserRepository;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserDto userDto) {
        // Check if the email already exists in the database. If it does, throw an exception.
        userRepository.findByEmail(userDto.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Email already exists");
                });
        // Create a new User entity and set its attributes from the provided UserDto
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));  // Encode the password before saving it
        user.setLastLoginDate(LocalDateTime.now());
        // Save the new user in the database and return the saved User object
        return userRepository.save(user);
    }
    // Method to update an existing user by their ID
    public User updateUser(Long id, UserDto userDto) {
        // Fetch the user by ID, if not found, throw an exception
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        // Update the user's name
        user.setName(userDto.getName());
        // Check if the email is changed and if the new email already exists. If so, throw an exception
        if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setEmail(userDto.getEmail());
        // Check if the email is changed and if the new email already exists. If so, throw an exception
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        // Save the updated user in the database and return the saved User object
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        // Check if the user exists, if not, throw an exception
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        // Delete the user by ID
        userRepository.deleteById(id);
    }
    // Method to fetch all users with pagination
    public Page<User> getAllUsers(Pageable pageable) {
        // Return all users with pagination
        return userRepository.findAll(pageable);
    }
    // Method to update the last login date of a user based on email
    public void updateLastLoginDate(String email) {
        // Fetch the user by email, if not found, throw an exception
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        // Update the last login date to the current time
        user.setLastLoginDate(LocalDateTime.now());
        // Save the updated user back to the database
        userRepository.save(user);
    }


}
