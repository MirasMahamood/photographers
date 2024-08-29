package com.app.photographers.integration.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static redis.embedded.RedisServer.newRedisServer;
import static redis.embedded.core.ExecutableProvider.REDIS_7_2_MACOSX_14_SONOMA_HANKCP;
import static redis.embedded.core.ExecutableProvider.newCachedUrlProvider;

@TestConfiguration
public class TestRedisConfig {

    private final RedisServer redisServer;

    public TestRedisConfig() throws IOException {
        final Path cacheLocation = Paths.get(System.getProperty("java.io.tmpdir"), "redis-binary");
        redisServer = newRedisServer()
                .executableProvider(newCachedUrlProvider(cacheLocation, REDIS_7_2_MACOSX_14_SONOMA_HANKCP))
                .build();
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() throws IOException {
        redisServer.stop();
    }
}
