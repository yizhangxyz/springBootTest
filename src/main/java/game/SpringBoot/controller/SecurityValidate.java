package game.SpringBoot.controller;


import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.ErrorCode;
import game.SpringBoot.common.MyHttpClient;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.message.ClientMessages.ErrorRsp;
import game.SpringBoot.message.ServerMessages.RequestValidate;
import game.SpringBoot.model.UserInfo;
import game.SpringBoot.services.UserService;


@Configuration
public class SecurityValidate extends WebMvcConfigurationSupport
{

    public final static String SESSION_KEY = "user";
    
    @Autowired
	private UserService userService;

    @Bean
    public SecurityInterceptor getSecurityInterceptor() 
    {
        return new SecurityInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry)
    {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/login");
        addInterceptor.excludePathPatterns("/test");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter
    {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception 
        {
            String postData = MyHttpClient.getPostData(request);
    		
            RequestValidate req = JSONObject.parseObject(postData, RequestValidate.class);
            
            if(req!= null && UserManager.getInstance().getUser(req.userToken) != null)
            {
            	request.getSession().setAttribute("userToken",req.userToken);
            	return true;
            }
            
            UserInfo userInfo = userService.findByToken(req.userToken);
            if(userInfo != null)
            {
            	UserManager.getInstance().addUser(req.userToken, userInfo);
            	request.getSession().setAttribute("userToken",req.userToken);
            	return true;
            }

            ErrorRsp rsp = new ErrorRsp();
        	rsp.errcode = ErrorCode.CODE_RELOGIN;
        	rsp.errmsg  = "please relogin by code.";
        	
            PrintWriter out = response.getWriter();
            out.append(JSONObject.toJSONString(rsp));

            return false;
        }
    }
}