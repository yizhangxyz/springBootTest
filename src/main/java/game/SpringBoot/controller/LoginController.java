package game.SpringBoot.controller;

import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.common.MessageCode;
import game.SpringBoot.common.MyHttpClient;
import game.SpringBoot.common.PropertiesConfig;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.message.ClientMessages.ErrorRsp;
import game.SpringBoot.message.ClientMessages.LoginCodeReq;
import game.SpringBoot.message.ClientMessages.UserLoginRsp;
import game.SpringBoot.message.ServerMessages.LoginFailed;
import game.SpringBoot.message.ServerMessages.LoginSuccess;
import game.SpringBoot.message.ServerMessages.RequestValidate;
import game.SpringBoot.model.UserInfo;
import game.SpringBoot.services.UserService;

@RestController
public class LoginController
{
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
    public @ResponseBody String login(HttpServletRequest request)
	{
		String codeJson = MyHttpClient.getPostData(request);
		LoginCodeReq loginCodeReq = JSONObject.parseObject(codeJson, LoginCodeReq.class);
		return doLogin(loginCodeReq);
    }
	
	@RequestMapping("/loginGet")
    public @ResponseBody String loginGet(String code,HttpServletRequest request)
	{
		LoginCodeReq loginCodeReq = new LoginCodeReq();
		loginCodeReq.code = code;
		return doLogin(loginCodeReq);
    }
	
	private String doLogin(LoginCodeReq loginCodeReq)
	{
		String wxAppId  = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_APPID");
		String wxSecret = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_SECRET");
		
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxAppId +"&secret=" + wxSecret + "&js_code=" + loginCodeReq.code + "&grant_type=authorization_code";
        String ret = MyHttpClient.doGet(url);
        
        LoginFailed failedInfo = JSONObject.parseObject(ret, LoginFailed.class);
        
        if(failedInfo.errcode > 0)
        {
        	LogUtils.getLogger().info("login by code ret="+ret); 
        	
        	ErrorRsp rsp = new ErrorRsp();
        	rsp.errcode = failedInfo.errcode;
        	rsp.errmsg  = failedInfo.errmsg;
        	
        	return JSONObject.toJSONString(rsp);

        }
        LoginSuccess successInfo = JSONObject.parseObject(ret, LoginSuccess.class);
        
        String token = generateToken();
        
        UserInfo userInfo = new UserInfo();
        
        userInfo.token = token;
        userInfo.session_key = successInfo.session_key;
        userInfo.openid = successInfo.openid;
        userInfo.updateTime = System.currentTimeMillis();
        userInfo.createTime = 0;
        
        try
		{
			userService.createOrUpdateUser(userInfo);
			UserManager.getInstance().addUser(token, userInfo);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			LogUtils.getLogger().info(e.toString());
			
			ErrorRsp rsp = new ErrorRsp();
        	rsp.errcode = MessageCode.DB_ERROR;
        	rsp.errmsg  = "account update error.";
        	
        	return JSONObject.toJSONString(rsp);
		}
        
        UserLoginRsp loginRsp = new UserLoginRsp();
        loginRsp.token = token;
        
		return JSONObject.toJSONString(loginRsp);
	}
	
	private String generateToken()
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	@PostMapping("/test")
    public @ResponseBody String test(HttpServletRequest request)
	{
		String tokenJson = MyHttpClient.getPostData(request);
		RequestValidate requestValidate = JSONObject.parseObject(tokenJson, RequestValidate.class);
		return doTest(requestValidate.token);
    }
	
	@RequestMapping("/testGet")
    public String testGet(String token)
	{
		return doTest(token);	   
    }
	
	private String doTest(String token)
	{
		try
		{
			UserInfo userInfo = userService.findByToken(token);
			if(userInfo != null)
			{
				return JSONObject.toJSONString(userInfo);
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ErrorRsp rsp = new ErrorRsp();
    	rsp.errcode = MessageCode.FAILED;
    	rsp.errmsg  = "no user.";
    	
    	return JSONObject.toJSONString(rsp);
	}
	
}
