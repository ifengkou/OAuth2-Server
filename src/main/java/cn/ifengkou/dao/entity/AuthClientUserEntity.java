package cn.ifengkou.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"openId"}))
public class AuthClientUserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String openId;
    @Column(nullable = false)
    private String clientId;
    @Column(nullable = false)
    private Long unionId;
}
