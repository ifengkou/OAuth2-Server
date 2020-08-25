package cn.ifengkou.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(callSuper = true)
@Data
public class UserAccount implements Serializable{
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private String mobile;
    private Integer status;
}
