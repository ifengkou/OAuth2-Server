package cn.ifengkou.model.exception;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
public class OAuth2Exception extends RuntimeException {
    public OAuth2Exception() {
        super();
    }

    public OAuth2Exception(String message) {
        super(message);
    }
}
