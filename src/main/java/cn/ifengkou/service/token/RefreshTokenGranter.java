package cn.ifengkou.service.token;

import cn.ifengkou.model.AuthClient;
import io.jsonwebtoken.Claims;
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
@Component
@Slf4j
public class RefreshTokenGranter implements TokenGranter {

    @Autowired
    KeyPair keyPair;

    @Override
    public Map<String, Object> grant(AuthClient client, Map<String, String> parameters) {

        Map<String, Object> result = new HashMap<>(16);
        result.put("status", 0);

        String refreshToken = parameters.get("refresh_token");

        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(refreshToken).getBody();
            Date now = new Date();
            Date tokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getAccessTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());
            Date refreshTokenExpiration = Date.from(LocalDateTime.now().plusSeconds(client.getRefreshTokenValidity()).atZone(ZoneId.systemDefault()).toInstant());
            String tokenId = UUID.randomUUID().toString();
            claims.setId(tokenId);
            claims.setIssuedAt(now);
            claims.setExpiration(refreshTokenExpiration);
            claims.setNotBefore(now);

            claims.put("jti", tokenId);
            String newRefreshToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(keyPair.getPrivate())
                .compact();

            claims.setId(tokenId);
            claims.setIssuedAt(now);
            claims.setExpiration(tokenExpiration);
            claims.setNotBefore(now);
            claims.remove("jti");
            String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(keyPair.getPrivate())
                .compact();

            result.put("access_token", accessToken);
            result.put("token_type", "bearer");
            result.put("refresh_token", newRefreshToken);
            result.put("expires_in", client.getAccessTokenValidity() - 1);
            result.put("accountOpenCode", claims.get("accountOpenCode"));
            result.put("scope", "user_info");
            result.put("jti", tokenId);
            result.put("status", 1);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.debug("exception", e);
            }
            throw e;
        }

        return result;
    }
}
