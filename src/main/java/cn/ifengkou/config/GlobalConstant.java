package cn.ifengkou.config;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/9/21
 */
public class GlobalConstant {
    /**
     * 返回成功
     */
    public final static String PWD_SALT = "abcd123";
    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * 授权页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";
    public static String SESSION_USER_ATTRIBUTE = "SESSION_USER_ATTRIBUTE";
}
