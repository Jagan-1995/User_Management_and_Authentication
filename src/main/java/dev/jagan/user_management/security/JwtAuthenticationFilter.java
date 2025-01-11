package dev.jagan.user_management.security;

import dev.jagan.user_management.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider; // A service to handle JWT token operations
    private CustomUserDetailsService customUserDetailsService; // Service to load user details from the database

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    // This method is called for each request to check if a JWT token is present and valid
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract the JWT token from the request
            String jwt = getJwtFromRequest(request);
            // If the JWT is not null and valid, set the authentication in the SecurityContext
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Get the user ID from the JWT token
                Long userId = tokenProvider.getUserIdFromJWT(jwt);
                // Load the user details using the custom user details service
                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                // Create an authentication token with the user details
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // Set details about the authentication, such as the HTTP request
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication token in the SecurityContext for the current request
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Log any exception that occurs while setting user authentication
            logger.error("Could not set user authentication in security context", ex);
        }
        // Continue the filter chain to pass the request along
        filterChain.doFilter(request, response);
    }
    // Helper method to extract the JWT token from the Authorization header of the request
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // If the Authorization header is present and starts with "Bearer ", extract the token
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove the "Bearer " prefix and return the token
        }
        return null; // If no token is found, return null
    }
}
