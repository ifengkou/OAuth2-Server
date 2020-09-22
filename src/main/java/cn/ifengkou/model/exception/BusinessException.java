package cn.ifengkou.model.exception;

/**
 * 业务逻辑异常
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
public class BussinessException extends RuntimeException {

    public BussinessException() {
        super();
    }

    public BussinessException(String message) {
        super(message);
    }
}
