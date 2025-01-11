package dev.jagan.user_management.config;

import dev.jagan.user_management.interceptor.RateLimitInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RateLimitingConfig rateLimitingConfig;

    public WebConfig(RateLimitingConfig rateLimitingConfig) {
        this.rateLimitingConfig = rateLimitingConfig;
    }

    /**
     * Add interceptors to handle specific request logic before controllers are invoked.
     * In this case, the RateLimitInterceptor is added for rate-limiting logic.
     *
     * @param registry The registry to add interceptors to.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RateLimitInterceptor(rateLimitingConfig)) // Add the rate-limiting interceptor
                .addPathPatterns("/api/auth/login"); // Apply rate-limiting to the login endpoint
    }
}
