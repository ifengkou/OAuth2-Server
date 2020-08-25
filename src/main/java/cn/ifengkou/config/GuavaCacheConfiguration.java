/*
package cn.ifengkou.config;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

*/
/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/24
 *//*

@Configuration
@EnableCaching
public class GuavaCacheConfiguration {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<GuavaCache> caches = new ArrayList<>();
        for (CachesEnum c : CachesEnum.values()) {
            caches.add(new GuavaCache(c.name(), CacheBuilder.newBuilder().recordStats()
                    .expireAfterWrite(c.getTtl(), TimeUnit.SECONDS)
                    .maximumSize(c.getMaxSize()).build()));
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
*/
