package cn.ifengkou.config;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/24
 */
public enum CachesEnum {
    /**
     * 使用默认值
     */
    DefaultCache,
    /**
     * 指定过期时间和最大容量
     */
    OAuth2ClientCache(60 * 60 * 2, 20),
    OAuth2AuthorizationCodeCache(60 * 3, 100000),
    OAuth2AuthorizationCodeFailureTimesCache(60 * 3, 100000),
            ;

    CachesEnum() {
    }

    CachesEnum(int ttl) {
        this.ttl = ttl;
    }

    CachesEnum(int ttl, int maxSize) {
        this.ttl = ttl;
        this.maxSize = maxSize;
    }

    private int maxSize = 100000;
    private int ttl = 60 * 5;

    public int getMaxSize() {
        return maxSize;
    }

    public int getTtl() {
        return ttl;
    }
}
