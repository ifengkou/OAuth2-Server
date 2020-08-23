package cn.ifengkou.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "int default 1")
    private int status;

    @Column(columnDefinition = "DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP")
    private Date createTime;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON update CURRENT_TIMESTAMP")
    private Date updateTime;
}
