package dev.jagan.user_management.controllers;


import dev.jagan.user_management.dtos.UserDto;
import dev.jagan.user_management.models.User;
import dev.jagan.user_management.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to retrieve all users (paginated)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Ensures only users with 'ADMIN' role can access this endpoint
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getCurrentUserId() == #id") // Ensures either an 'ADMIN' or the user themselves can update their own details
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getCurrentUserId() == #id")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
