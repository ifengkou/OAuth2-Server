package cn.ifengkou.model.exception;

import lombok.Data;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@Data
public class ExpiredException extends RuntimeException {
    public ExpiredException() {
        super();
    }

    public ExpiredException(String message) {
        super(message);
    }
}
