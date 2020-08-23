package cn.ifengkou.model;

import lombok.*;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/22
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthToken {
    /**
     * 标准参数
     */
    private String accessToken;
    private Long expireIn;
    private String refreshToken;
    private String scope;
    private String tokenType;

    private String idToken;
    private String openId;
    private String jti;
}
