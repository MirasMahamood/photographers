package com.app.photographers.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

@Configuration
@Slf4j
public class CachingConfig implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {
        return new RedisCacheErrorHandler();
    }

    public static class RedisCacheErrorHandler implements CacheErrorHandler {

        @Override
        public void handleCacheGetError(RuntimeException exception, Cache cache, @NonNull Object key) {
            log.warn("Unable to get from cache {} : {}", cache.getName(), exception.getMessage());
        }

        @Override
        public void handleCachePutError(RuntimeException exception, Cache cache, @NonNull Object key, Object value) {
            log.warn("Unable to put into cache {} : {}", cache.getName(), exception.getMessage());
        }

        @Override
        public void handleCacheEvictError(RuntimeException exception, Cache cache, @NonNull Object key) {
            log.warn("Unable to evict from cache {} : {}", cache.getName(), exception.getMessage());
        }

        @Override
        public void handleCacheClearError(RuntimeException exception, Cache cache) {
            log.warn("Unable to clean cache {} : {}", cache.getName(), exception.getMessage());
        }
    }
}
