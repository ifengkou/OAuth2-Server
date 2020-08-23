package cn.ifengkou.model.exception;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数异常类
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@Data
public class ParamsException extends RuntimeException {
    private Map<String,String> details;
    public ParamsException(){
        super();
    }
    public ParamsException(Map<String,String> details){
        this.details = details;
    }
    public ParamsException(String key, String msg) {
        Map<String, String> map =new HashMap<>();
        map.put(key, msg);
        this.details = map;
    }
}
