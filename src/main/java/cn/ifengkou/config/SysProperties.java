package cn.ifengkou.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Configuration
public class SysProperties {
    @Value("${oauth2.issuer-uri:http://localhost:8888}")
    String issuerUri;

    public static String SESSION_USER_ATTRIBUTE = "SESSION_USER_ATTRIBUTE";

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 授权页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";
}
