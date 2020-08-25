package cn.ifengkou.config;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/24
 */
public enum GrantTypeEnum {
    /**
     * 使用默认值
     */
    password,
    /**
     * 过期。最大容量使用默认值
     */
    authorization_code,
    /**
     * 指定过期时间和最大容量
     */
    refresh_token;

    GrantTypeEnum() {
    }
}
