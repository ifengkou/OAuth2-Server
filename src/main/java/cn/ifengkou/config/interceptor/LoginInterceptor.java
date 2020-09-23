package cn.ifengkou.config.interceptor;

import cn.ifengkou.config.GlobalConstant;
import cn.ifengkou.config.SysProperties;
import cn.ifengkou.model.UserAccount;
import cn.ifengkou.utils.HttpUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author shenlongguang<https://github.com/ifengkou>
 * @date: 2020/8/25
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的token
        UserAccount user = (UserAccount) session.getAttribute(GlobalConstant.SESSION_USER_ATTRIBUTE);

        if(user != null){
            return true;
        }else{
            //如果token不存在，则跳转到登录页面
            String redirectUrl = HttpUtils.getRequestUrl(request);
            response.sendRedirect(request.getContextPath() + "/signIn?redirect_uri=" + redirectUrl);
            return false;
        }
    }
}
