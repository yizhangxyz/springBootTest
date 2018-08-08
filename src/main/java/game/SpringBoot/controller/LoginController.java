package game.SpringBoot.controller;

import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.ErrorCode;
import game.SpringBoot.common.LogUtils;
import game.SpringBoot.common.MyHttpClient;
import game.SpringBoot.common.PropertiesConfig;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.message.ClientMessages.ErrorRsp;
import game.SpringBoot.message.ClientMessages.LoginCodeReq;
import game.SpringBoot.message.ClientMessages.UserLoginRsp;
import game.SpringBoot.message.ServerMessages.LoginFailed;
import game.SpringBoot.message.ServerMessages.LoginSuccess;
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
		String postData = MyHttpClient.getPostData(request);
		
		LoginCodeReq codeData = JSONObject.parseObject(postData, LoginCodeReq.class);
		
		String wxAppId  = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_APPID");
		String wxSecret = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_SECRET");
		
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxAppId +"&secret=" + wxSecret + "&js_code=" + codeData.code + "&grant_type=authorization_code";
        String ret = MyHttpClient.doGet(url);
        
        LoginFailed failedInfo = JSONObject.parseObject(ret, LoginFailed.class);
        
        if(failedInfo.errcode > 0)
        {
        	LogUtils.getLogger().info("ret="+ret); 
        	
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
        	rsp.errcode = ErrorCode.DB_ERROR;
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
	
}
