package cn.ifengkou.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Configuration
public class SysProperties {
    @Value("${oauth2.issuer-uri:http://localhost:8888}")
    String issuerUri;

}
