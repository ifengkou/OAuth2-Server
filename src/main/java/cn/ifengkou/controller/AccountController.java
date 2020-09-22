package cn.ifengkou.controller;

import cn.ifengkou.config.SysProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/25
 */
@Controller
public class AccountController {
    @RequestMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request){
        String redirectUrl = request.getParameter("redirectUri");
        if(StringUtils.isNoneBlank(redirectUrl)){
            HttpSession session = request.getSession();
            //将回调地址添加到session中
            session.setAttribute(SysProperties.SESSION_LOGIN_REDIRECT_URL,redirectUrl);
        }

        return new ModelAndView("login");
    }
}
