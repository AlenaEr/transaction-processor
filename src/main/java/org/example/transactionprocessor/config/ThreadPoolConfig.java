package org.example.transactionprocessor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Value("${app.threadPool.corePoolSize}")
    private int corePoolSize;

    @Value("${app.threadPool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${app.threadPool.queueCapacity}")
    private int queueCapacity;

    @Bean
    public ThreadPoolTaskExecutor transactionExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("Transaction-Executor-");
        executor.initialize();
        return executor;
    }
}
