package cn.ifengkou.dao.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户端实体
 * client_id,client_name,client_secret,desc,home_url,callback_url,logo,background,status(状态),type类型,auth_required释放需要用户认证)
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Data
public class AuthClientEntity extends BaseEntity implements Serializable{
    private String clientId;
    private String clientName;
    private String clientSecret;
    private String desc;
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
