package cn.ifengkou.service.token;

import cn.ifengkou.config.GrantTypeEnum;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/24
 */
@AllArgsConstructor
@Component
public class TokenFactory {
    @Autowired
    PasswordTokenGranter passwordTokenGranter;
    @Autowired
    RefreshTokenGranter refreshTokenGranter;
    @Autowired
    AuthorizationCodeTokenGranter authorizationCodeTokenGranter;

    public TokenGranter getTokenGranter(String grant_type){
        if (StringUtils.equalsIgnoreCase(grant_type, GrantTypeEnum.password.name())) {
            return passwordTokenGranter;
        } else if (StringUtils.equalsIgnoreCase(grant_type, GrantTypeEnum.authorization_code.name())) {
            return authorizationCodeTokenGranter;
        } else if (StringUtils.equalsIgnoreCase(grant_type, GrantTypeEnum.refresh_token.name())) {
            return refreshTokenGranter;
        }
        return null;
    }
}
