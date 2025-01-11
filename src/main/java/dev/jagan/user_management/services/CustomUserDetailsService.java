package dev.jagan.user_management.services;

import dev.jagan.user_management.models.User;
import dev.jagan.user_management.repositories.UserRepository;
import dev.jagan.user_management.security.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to load user details by username (email in this case)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user from database using the provided email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->  // If no user found, throw UsernameNotFoundException
                        new UsernameNotFoundException("User not found with email : " + email)
                );
        // Convert User entity to UserPrincipal (custom UserDetails implementation) and return it
        return UserPrincipal.create(user);
    }
    // Method to load user details by user ID
    public UserDetails loadUserById(Long id) {
        // Fetch user from database using the provided ID
        User user = userRepository.findById(id)
                .orElseThrow(() ->  // If no user found, throw UsernameNotFoundException
                        new UsernameNotFoundException("User not found with id : " + id)
                );
        // Convert User entity to UserPrincipal (custom UserDetails implementation) and return it
        return UserPrincipal.create(user);
    }
}
