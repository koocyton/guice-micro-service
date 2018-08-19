package com.doopp.gauss.server.configuration;

import com.doopp.gauss.common.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class CommonConfiguration {

    @Bean
    public ApplicationProperties applicationProperties() {
       return new ApplicationProperties();
    }

    @Bean
    public IdWorker userIdWorker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public ThreadPoolTaskExecutor gameTaskExecutor () {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(5);
        threadPoolTaskExecutor.setMaxPoolSize(1000);
        threadPoolTaskExecutor.setQueueCapacity(25);
        threadPoolTaskExecutor.setKeepAliveSeconds(30000);
        return threadPoolTaskExecutor;
    }

//    @Bean
//    public OnlineCheckTask onlineCheckTask(ThreadPoolTaskExecutor gameTaskExecutor) {
//        return new OnlineCheckTask(gameTaskExecutor);
//    }
}
