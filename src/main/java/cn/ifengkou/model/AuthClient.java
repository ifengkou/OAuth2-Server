package cn.ifengkou.model;


import lombok.*;

import java.io.Serializable;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/23
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthClient implements Serializable{
    private Long id;
    private String clientId;
    private String clientName;
    private String clientSecret;
    private String remarks;
    private String homeUrl;
    private String callbackUrl;
    private String logoutUrl;
    private String logo;
    private String background;
    @Builder.Default
    private int accessTokenValidity = 60 * 60 * 2;
    @Builder.Default
    private int refreshTokenValidity = 60 * 60 * 24;
    private int status;
    private boolean authRequired;
}
