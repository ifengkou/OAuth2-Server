package cn.ifengkou.dao.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * 客户端实体
 * client_id,client_name,client_secret,remarks,home_url,callback_url,logoutUrl,logo,background,status(状态),type类型,auth_required释放需要用户认证)
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"clientId"}))
public class AuthClientEntity extends BaseEntity{
    @Column(nullable = false)
    private String clientId;
    private String clientName;
    @Column(nullable = false)
    private String clientSecret;
    private String remarks;
    private String homeUrl;
    private String callbackUrl;
    /**
     * 全局登出时 由IDP 主动调用 SP的退出url。备用
     */
    private String logoutUrl;
    private String logo;
    private String background;
    private Integer type;
    private boolean authRequired;
}
