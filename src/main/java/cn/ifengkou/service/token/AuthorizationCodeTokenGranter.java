package cn.ifengkou.service.token;

import cn.ifengkou.config.CachesEnum;
import cn.ifengkou.config.SysProperties;
import cn.ifengkou.model.AuthClient;
import cn.ifengkou.model.UserAccount;
import cn.ifengkou.model.exception.OAuth2Exception;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
public class AuthorizationCodeTokenGranter implements TokenGranter {

    @Autowired
    KeyPair keyPair;

    @Autowired
    SysProperties sysProperties;

    @Autowired
    CacheManager cacheManager;

    @Override
    public Map<String, Object> grant(AuthClient client, Map<String, String> parameters) {

        Map<String, Object> result = new HashMap<>(16);
        result.put("status", 0);

        String authorizationCode = parameters.get("code");
        String clientId = parameters.get("client_id");
        String scope = parameters.get("scope");

        String redirect_uri = parameters.get("redirect_uri");
        if (StringUtils.isEmpty(redirect_uri) || !StringUtils.equalsIgnoreCase(client.getCallbackUrl(), redirect_uri)) {
            result.put("status", 0);
            result.put("code", "invalid_redirect_uri");
            result.put("message", "invalid_redirect_uri");
        }
        if (authorizationCode == null) {
            throw new OAuth2Exception("An authorization code must be supplied.");
        }
        Cache.ValueWrapper storedCode = cacheManager.getCache(CachesEnum.OAuth2AuthorizationCodeCache.name()).get(authorizationCode);
        if (storedCode != null) {

            UserAccount userInfo = (UserAccount) (storedCode.get());

            Date now = new Date();
            Date tokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getAccessTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());
            Date refreshTokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getRefreshTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());


            String tokenId = UUID.randomUUID().toString();
            String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("accountOpenCode", userInfo.getId())
                .setIssuer(sysProperties.getIssuerUri())
                .setSubject(userInfo.getUsername())
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
                .claim("accountOpenCode", userInfo.getId())
                .claim("jti", tokenId)
                .setIssuer(sysProperties.getIssuerUri())
                .setSubject(userInfo.getUsername())
                .setAudience(clientId)
                // TODO 根据具体情况附带 权限/角色
                //.claim("roles", userInfo.getAuthorities().stream().map(e -> e.getAuthority()).collect(Collectors.toList()))
                .setExpiration(refreshTokenExpiration)
                .setNotBefore(now)
                .setIssuedAt(now)
                .setId(UUID.randomUUID().toString())
                .signWith(keyPair.getPrivate())
                .compact();

            cacheManager.getCache(CachesEnum.OAuth2AuthorizationCodeCache.name()).evictIfPresent(authorizationCode);

            result.put("access_token", accessToken);
            result.put("token_type", "bearer");
            result.put("refresh_token", refreshToken);
            result.put("expires_in", client.getAccessTokenValidity() - 1);
            result.put("accountOpenCode", userInfo.getId());
            result.put("scope", scope);
            result.put("jti", tokenId);
            result.put("status", 1);
            return result;
        } else {
            throw new OAuth2Exception("An authorization code must be supplied.");
        }
    }
}
