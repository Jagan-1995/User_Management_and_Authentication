package dev.jagan.user_management.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class RateLimitingConfig {

    // A thread-safe map to store rate-limiting buckets for different keys (e.g., users, IPs)
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Expose the buckets map as a Spring Bean.
     * This allows other components to access and manage the rate-limiting buckets.
     */
    @Bean
    public Map<String, Bucket> buckets() {
        return buckets;
    }

    /**
     * Get or create a bucket for a given key.
     * If a bucket for the key doesn't exist, a new one is created.
     *
     * @param key The key to identify the bucket (e.g., user ID or IP address).
     * @return The bucket associated with the given key.
     */
    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::newBucket);
    }

    /**
     * Create a new bucket with rate-limiting rules.
     * The bucket allows 10 requests per minute.
     *
     * @param key The key to associate with the new bucket.
     * @return A new bucket with rate-limiting settings.
     */
    private Bucket newBucket(String key) {
        // Define refill rules: Allow 10 tokens to be refilled every 1 minute
        Refill refill = Refill.intervally(10, Duration.ofMinutes(1));
        // Define the bandwidth limit: Maximum 10 tokens at any time
        Bandwidth limit = Bandwidth.classic(10, refill);
        // Build and return the bucket with the specified rules
        return Bucket.builder().addLimit(limit).build();
    }
}
