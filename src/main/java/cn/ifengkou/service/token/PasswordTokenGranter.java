package cn.ifengkou.service.token;

import cn.ifengkou.config.SysProperties;
import cn.ifengkou.model.AuthClient;
import cn.ifengkou.model.UserAccount;
import cn.ifengkou.model.exception.OAuth2Exception;
import cn.ifengkou.service.UserAccountService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/24
 */
@Slf4j
@Component
public class PasswordTokenGranter implements TokenGranter {
    @Autowired
    KeyPair keyPair;
    @Autowired
    UserAccountService userAccountService;

    @Autowired
    SysProperties sysProperties;

    @Override
    public Map<String, Object> grant(AuthClient client, Map<String, String> parameters) {

        Map<String, Object> result = new HashMap<>(16);
        result.put("status", 0);

        String username = parameters.get("username");
        String password = parameters.get("password");
        String clientId = parameters.get("client_id");
        String scope = parameters.get("scope");

        UserAccount account = userAccountService.findByUsername(username);
        if(account== null || !password.equals(account.getPassword())){
            throw new OAuth2Exception("用户名密码错误");
        }
        if(account.getStatus()<=0){
            throw new OAuth2Exception("用户名已失效");
        }

        Date now = new Date();
        Date tokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getAccessTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());
        Date refreshTokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getAccessTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());

        String tokenId = UUID.randomUUID().toString();
        String accessToken = Jwts.builder()
            .setHeaderParam("alg", "HS256")
            .setHeaderParam("typ", "JWT")
            .claim("accountOpenCode", account.getId())
            .setIssuer(sysProperties.getIssuerUri())
            .setSubject(account.getUsername())
            .setAudience(clientId)
            //.claim("roles", userInfo.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList()))
            .setExpiration(tokenExpiration)
            .setNotBefore(now)
            .setIssuedAt(now)
            .setId(tokenId)
            .signWith(keyPair.getPrivate())
            .compact();

        String refreshToken = Jwts.builder()
            .setHeaderParam("alg", "HS256")
            .setHeaderParam("typ", "JWT")
            .claim("accountOpenCode", account.getId())
            .claim("jti", tokenId)
            .setIssuer(sysProperties.getIssuerUri())
            .setSubject(account.getUsername())
            .setAudience(clientId)
            //.claim("roles", userInfo.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList()))
            .setExpiration(refreshTokenExpiration)
            .setNotBefore(now)
            .setIssuedAt(now)
            .setId(UUID.randomUUID().toString())
            .signWith(keyPair.getPrivate())
            .compact();

        result.put("access_token", accessToken);
        result.put("token_type", "bearer");
        result.put("refresh_token", refreshToken);
        result.put("expires_in", client.getAccessTokenValidity() - 1);
        result.put("accountOpenCode", account.getId());
        result.put("scope", scope);
        result.put("jti", tokenId);
        result.put("status", 1);
        return result;
    }
}
