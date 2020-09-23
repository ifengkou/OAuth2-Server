package cn.ifengkou.controller;

import cn.ifengkou.config.CachesEnum;
import cn.ifengkou.config.GlobalConstant;
import cn.ifengkou.dao.entity.AuthScopeEntity;
import cn.ifengkou.model.AuthClient;
import cn.ifengkou.model.UserAccount;
import cn.ifengkou.service.AuthClientService;
import cn.ifengkou.service.AuthScopeService;
import cn.ifengkou.service.token.TokenFactory;
import cn.ifengkou.service.token.TokenGranter;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/25
 */
@Controller
@RequestMapping("/oauth")
@Slf4j
public class OAuth2Controller {
    @Autowired
    AuthClientService authClientService;

    @Autowired
    AuthScopeService authScopeService;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    TokenFactory tokenFactory;

    @Autowired
    KeyPair keyPair;

    @GetMapping("/authorize")
    public String getAccessToken(ModelMap model,
                                 HttpSession session,
                                 @RequestHeader(name = "referer", required = false) String referer,
                                 @RequestParam(value = "client_id") String client_id,
                                 @RequestParam(value = "response_type") String response_type,
                                 @RequestParam(value = "state", required = false) String state,
                                 @RequestParam(value = "scope", required = false) String scopes,
                                 @RequestParam(value = "redirect_uri") String redirect_uri) {
        AuthClient client = authClientService.findByClientId(client_id);

        if (client == null || !StringUtils.equalsIgnoreCase(client.getCallbackUrl(), redirect_uri)) {
            if (redirect_uri.indexOf("?") > 0) {
                //TODO error 可以交给Server 端展示异常
                return "redirect:" + redirect_uri + "&error=invalid_client";
            } else {
                return "redirect:" + redirect_uri + "?error=invalid_client";
            }
        }

        if (client.isAuthRequired()) {
            model.put("client_id", client_id);
            model.put("client_name", client.getClientName());
            model.put("from", referer);
            model.put("state", state);
            model.put("redirect_uri", redirect_uri);
            Map<String, String> scopeMap = new LinkedHashMap<>();
            for (String scope : scopes.split(",")) {
                AuthScopeEntity scopeDefinition = authScopeService.findByScope(scope);
                if (scopeDefinition != null) {
                    scopeMap.put("scope." + scope, scopeDefinition.getDefinition());
                } else {
                    scopeMap.put("scope." + scope, scope);
                }
            }
            model.put("scopeMap", scopeMap);

            return "accessConfirmation";
        } else {
            // uuid 即是授权码
            String uuid = UUID.randomUUID().toString().replace("-", "");
            UserAccount account = (UserAccount) session.getAttribute(GlobalConstant.SESSION_USER_ATTRIBUTE);
            cacheManager.getCache(CachesEnum.OAuth2AuthorizationCodeCache.name()).put(uuid, account);
            if (client.getCallbackUrl().indexOf("?") > 0) {
                return "redirect:" + client.getCallbackUrl() + "&code=" + uuid + "&state=" + state;
            } else {
                return "redirect:" + client.getCallbackUrl() + "?code=" + uuid + "&state=" + state;
            }
        }
    }

    @PostMapping("/authorize")
    public String postAccessToken(ModelMap model,
                                  HttpSession session,
                                  @RequestParam(name = "referer", required = false) String referer,
                                  @RequestParam(value = "client_id") String client_id,
                                  @RequestParam(value = "response_type", required = false) String response_type,
                                  @RequestParam(value = "state", required = false) String state,
                                  @RequestParam(value = "scope", required = false) String scope,
                                  @RequestParam(value = "user_oauth_approval", required = false, defaultValue = "false") boolean userOauthApproval,
                                  @RequestParam(value = "redirect_uri") String redirect_uri) {
        AuthClient client = authClientService.findByClientId(client_id);
        model.put("client_id", client_id);
        model.put("client_name", client.getClientName());
        model.put("from", referer);

        if (userOauthApproval) {
            // uuid 即是授权码
            String uuid = UUID.randomUUID().toString().replace("-", "");
            UserAccount account = (UserAccount) session.getAttribute(GlobalConstant.SESSION_USER_ATTRIBUTE);
            cacheManager.getCache(CachesEnum.OAuth2AuthorizationCodeCache.name()).put(uuid, account);
            if (client.getCallbackUrl().indexOf("?") > 0) {
                return "redirect:" + client.getCallbackUrl() + "&code=" + uuid + "&state=" + state;
            } else {
                return "redirect:" + client.getCallbackUrl() + "?code=" + uuid + "&state=" + state;
            }
        } else {
            if (redirect_uri.indexOf("?") > 0) {
                return "redirect:" + redirect_uri + "&state=" + state + "&error=not_approval";
            } else {
                return "redirect:" + redirect_uri + "&state=" + state + "?error=not_approval";
            }
        }
    }

    @PostMapping("/access_token")
    public ResponseEntity<Map<String, Object>> getAccessToken(@RequestParam(value = "client_id", required = false) String client_id,
                                                              @RequestParam(value = "client_secret", required = false) String client_secret,
                                                              @RequestParam(value = "grant_type") String grant_type,
                                                              @RequestParam(value = "scope", required = false) String scope,
                                                              @RequestParam(value = "redirect_uri", required = false) String redirect_uri,
                                                              @RequestParam(value = "refresh_token", required = false) String refresh_token,
                                                              @RequestParam(value = "code", required = false) String code,
                                                              @RequestParam(value = "username", required = false) String username,
                                                              @RequestParam(value = "password", required = false) String password) {
        Map<String, Object> result = new HashMap<>(16);
        AuthClient client = authClientService.findByClientId(client_id);
        HttpHeaders headers = new HttpHeaders();

        if (client == null) {
            result.put("status", 0);
            result.put("code", "invalid_client");
            result.put("message", "invalid_client");
            return new ResponseEntity<>(result, headers, HttpStatus.BAD_REQUEST);
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", client_id);
        parameters.put("client_secret", client_secret);
        parameters.put("grant_type", grant_type);
        parameters.put("scope", scope);
        parameters.put("redirect_uri", redirect_uri);
        parameters.put("refresh_token", refresh_token);
        parameters.put("code", code);
        parameters.put("username", username);
        parameters.put("password", password);

        TokenGranter tokenGranter = tokenFactory.getTokenGranter(grant_type);
        if(tokenGranter == null){
            result.put("status", 0);
            result.put("message", "不支持的grant类型");
        }else{
            result = tokenGranter.grant(client, parameters);
        }
        return new ResponseEntity<>(result, headers, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/verify_token")
    public Map<String, Object> checkAccessToken(@RequestParam(value = "access_token") String access_token) {
        Map<String, Object> result = new HashMap<>(16);
        try {
            Jwts.parserBuilder().setSigningKey(keyPair.getPublic()).build().parseClaimsJws(access_token).getBody();
            result.put("status", 1);
        } catch (Exception e) {
            result.put("status", 0);
            result.put("message", "access_token 无效");
            if (log.isErrorEnabled()) {
                log.error("access_token", e);
            }
        }
        return result;
    }


}
