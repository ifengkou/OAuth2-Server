package cn.ifengkou.model.exception;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
public class NotExistsException extends RuntimeException {

    public NotExistsException() {
        super();
    }

    public NotExistsException(String message) {
        super(message);
    }
}
