package cn.ifengkou.dao.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
public class BaseEntity implements Serializable{
    private Long id;
    private Date updateTime;
    private Date createTime;
    private int status;
}
