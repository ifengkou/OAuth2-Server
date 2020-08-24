package cn.ifengkou.test.utils;

import io.jsonwebtoken.*;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/24
 */
public class JwtUtilTest {
    static KeyPair keyPair;
    static String keyPassword="key_password";

    static {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new ClassPathResource("jwt.jks").getInputStream(), keyPassword.toCharArray());
            Enumeration<String> aliases = keyStore.aliases();
            String alias = null;
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());
            java.security.cert.Certificate certificate = keyStore.getCertificate(alias);
            PublicKey publicKey = certificate.getPublicKey();
            keyPair = new KeyPair(publicKey, privateKey);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Ignore
    public void testJwt() {
        Date now = new Date();
        Date tokenExpiration = Date.from(LocalDateTime.now().plusSeconds(2 * 3600).atZone(ZoneId.systemDefault()).toInstant());
        String userName = "logan";
        String OpenId = "abcd";
        String tokenId = UUID.randomUUID().toString();
        String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("OpenId", OpenId)
                .setIssuer("localhost:8888")
                .setSubject(userName)
                .setAudience("1234")
                .setExpiration(tokenExpiration)
                .setNotBefore(now)
                .setIssuedAt(now)
                .setId(tokenId)
                .signWith(keyPair.getPrivate())
                .compact();

        System.out.println(accessToken);

        JwtParser parser = Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build();
        Jws<Claims> claimsJws = parser.parseClaimsJws(accessToken);
        JwsHeader header = claimsJws.getHeader();
        Claims body = claimsJws.getBody();

        System.out.println("jwt header:" + header);
        System.out.println("jwt body:" + body);

    }
}
