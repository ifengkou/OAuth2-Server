package cn.ifengkou.controller;


import cn.ifengkou.config.GlobalConstant;
import cn.ifengkou.model.UserAccount;
import cn.ifengkou.model.exception.BusinessException;
import cn.ifengkou.service.AuthClientService;
import cn.ifengkou.service.UserAccountService;
import cn.ifengkou.utils.HttpUtils;
import org.apache.commons.codec.digest.Sha2Crypt;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.checkerframework.common.value.qual.MinLen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class SignInAndUpController {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserAccountService userAccountService;

    @Autowired
    AuthClientService authClientService;

    @GetMapping("/signIn")
    public String signIn(HttpServletRequest request,
                         @RequestParam(value = "error", required = false) String error,
                         Model model) {
        if (StringUtils.isNotEmpty(error)) {
            model.addAttribute("error", error);
        }

        String redirectUrl = request.getParameter("redirect_uri");
        if (StringUtils.isNoneBlank(redirectUrl)) {
            HttpSession session = request.getSession();
            //将回调地址添加到session中
            session.setAttribute(GlobalConstant.SESSION_LOGIN_REDIRECT_URL, redirectUrl);
        }

        return "signIn";
    }

    @PostMapping("/security_check")
    public void check(HttpServletRequest request, HttpServletResponse response) throws IOException {


        //用户名
        String username = request.getParameter("username");
        //密码
        String password = request.getParameter("password");

        if (StringUtils.isNoneBlank(username) && StringUtils.isNoneBlank(password)) {
            //1. 登录验证
            UserAccount user = userAccountService.findByUsername(username);
            //登录验证通过
            if (user.getPassword().equals(password)) {
                if (user.getStatus() != 0) {
                    throw new BusinessException("账户状态异常，请联系管理员");
                }

                //2. session中添加用户信息
                HttpSession session = request.getSession();
                session.setAttribute(GlobalConstant.SESSION_USER_ATTRIBUTE, user);

                //3. 返回给页面的数据
                //登录成功之后的回调地址
                String redirectUrl = (String) session.getAttribute(GlobalConstant.SESSION_LOGIN_REDIRECT_URL);
                session.removeAttribute(GlobalConstant.SESSION_LOGIN_REDIRECT_URL);

                Map<String, Object> data = new HashMap<>(1);
                if (StringUtils.isNoneBlank(redirectUrl)) {
                    data.put("redirect_uri", redirectUrl);
                    response.sendRedirect(redirectUrl);
                }
                response.sendRedirect(request.getContextPath());
                //return HttpUtils.buildJsonResponse("OK",data);
            }
        }
        response.sendRedirect(request.getContextPath() + "/signIn");
        //return HttpUtils.buildJsonResponse(request, HttpStatus.FORBIDDEN,"登录信息错误");
    }

    @GetMapping("/signUp")
    public String signUp(@RequestParam(value = "error", required = false) String error,
                         Model model) {
        if (StringUtils.isNotEmpty(error)) {
            model.addAttribute("error", error);
        }
        return "signUp";
    }

    @ResponseBody
    @PostMapping("/oauth/signUp")
    public ResponseEntity<Object> handleOauthSignUp(@RequestParam(value = "username") @Validated @MinLen(6) String username,
                                                 @RequestParam(value = "password") @Validated @MinLen(6) String password) {
        username = StringUtils.trimToEmpty(username).toLowerCase();
        password = StringUtils.trimToEmpty(password);
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(StringEscapeUtils.escapeHtml4(username));
        userAccount.setPassword(Sha2Crypt.sha256Crypt(password.getBytes(), GlobalConstant.PWD_SALT));
        userAccount.setOpenId(UUID.randomUUID().toString());
        userAccountService.create(userAccount);
        return HttpUtils.buildJsonResponse("OK",null);
    }
}
