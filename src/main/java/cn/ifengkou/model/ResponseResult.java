package cn.ifengkou.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/9/22
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    /**
     * 请求状态是否成功
     */
    private int code;
    /**
     * 详细的信息
     */
    private String msg;
    private String path;
    /**
     * 时间戳
     */
    private Long timestamp;
    /**
     * 返回对象
     */
    private T data;
}
