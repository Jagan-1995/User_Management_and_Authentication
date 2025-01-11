package dev.jagan.user_management.interceptor;

import dev.jagan.user_management.config.RateLimitingConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitingConfig rateLimitingConfig;

    public RateLimitInterceptor(RateLimitingConfig rateLimitingConfig) {
        this.rateLimitingConfig = rateLimitingConfig;
    }

    // Method called before the controller method is executed
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String ip = request.getRemoteAddr(); // Get the IP address of the incoming request
        Bucket bucket = rateLimitingConfig.resolveBucket(ip); // Get the rate limit bucket for the given IP
        // Check if the request can be processed based on the rate limit configuration
        if (!bucket.tryConsume(1)) { // Try to consume 1 token (i.e., process one request)
            // If the rate limit is exceeded, set the status to 429 Too Many Requests
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return false; // Prevent further processing of the request
        }

        return true; // Allow the request to proceed if rate limit is not exceeded
    }
}
