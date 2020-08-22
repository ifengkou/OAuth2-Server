package cn.ifengkou.dao.entity;

import lombok.Data;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
public class AuthScopeEntity extends BaseEntity {
    private String scope;
    /**
     * 定义 解释
     */
    private String definition;
}
