package cn.ifengkou.dao.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
public class AuthClientUserEntity implements Serializable {
    private Long openId;
    private Long clientId;
    private Long unionId;
}
