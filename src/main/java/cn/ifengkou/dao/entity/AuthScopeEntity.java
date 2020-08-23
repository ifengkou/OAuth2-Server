package cn.ifengkou.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"scope"}))
public class AuthScopeEntity extends BaseEntity {
    private String scope;
    /**
     * 定义 解释
     */
    private String definition;
}
