package com.tutu.contsant;

import lombok.Data;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
@EnableCaching
@EnableAsync
@Data
public class AppConfiguration {


    @Bean
    public CacheManager getEhCacheManager() {
        net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager();
        Cache cache=new Cache("JDGOODS",1000,false,false,3600,0);
        cacheManager.addCache(cache);
        Cache cache1=new Cache("TBGOODS",1000,false,false,5400,0);
        cacheManager.addCache(cache1);
        return cacheManager;
    }
    //把上面的ehcache的cachemanager 传入springcachemanager中
    @Bean
    public org.springframework.cache.CacheManager springCacheManager() {
        return new EhCacheCacheManager(getEhCacheManager());
    }

    /**
     * TODO: 此方法名称为asyncPromiseExecutor，即在spring中注入了一个名字为asyncPromiseExecutor的bean
     * 方法名只要在项目中唯一性，可以适当任意取（最好遵循一定的规则）
     * 使用方法：在需要加入线程池的方法上增加注解@Async("asyncPromiseExecutor")就可以加入此线程池异步执行
     *
     * @return
     * @throws
     * @author zhaoxi
     * @time 2018/11/16 14:36
     * @params
     */
    @Bean
    public ThreadPoolTaskExecutor asyncPromiseExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(10);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(99999);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("async-promise-");
        /**
         * rejection-policy：当pool已经达到max size的时候，如何处理新任务
         * CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化执行器
        executor.initialize();
        return executor;
    }

    public AppConfiguration() {
    }
}
