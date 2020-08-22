package cn.ifengkou.dao.entity;

import lombok.Data;

/**
 * 用户
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
public class UserEntity extends BaseEntity {
    private String username;

    private String password;

    private String mobile;

    private String email;
}
