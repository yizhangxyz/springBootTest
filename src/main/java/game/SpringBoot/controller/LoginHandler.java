package game.SpringBoot.controller;

import java.sql.SQLException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.common.MyHttpClient;
import game.SpringBoot.common.PropertiesConfig;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.message.ClientMessages.LoginMsgData;
import game.SpringBoot.message.ClientMessages.UserLoginRsp;
import game.SpringBoot.message.MessageCode;
import game.SpringBoot.message.ServerMessages.LoginFailed;
import game.SpringBoot.message.ServerMessages.LoginSuccess;
import game.SpringBoot.model.UserInfo;
import game.SpringBoot.services.UserService;

@Component
public class LoginHandler
{
	
	@Autowired
	private UserService userService;
	
	
    public String onLogin(UserInfo userInfo,String msgData)
	{
    	LoginMsgData loginCodeReq = JSONObject.parseObject(msgData, LoginMsgData.class);
		return doLogin(loginCodeReq);
    }

	private String doLogin(LoginMsgData loginMsgData)
	{
		String wxAppId  = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_APPID");
		String wxSecret = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_SECRET");
		
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + wxAppId +"&secret=" + wxSecret + "&js_code=" + loginMsgData.code + "&grant_type=authorization_code";
        String ret = MyHttpClient.doGet(url);
        
        LoginFailed failedInfo = JSONObject.parseObject(ret, LoginFailed.class);
        
        if(failedInfo.errcode > 0)
        {
        	LogUtils.getLogger().info("login by code ret="+ret); 
        	
        	UserLoginRsp rsp = new UserLoginRsp();
        	rsp.result_code = failedInfo.errcode;
        	rsp.msg  = failedInfo.errmsg;
        	rsp.token = "";
        	
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
        userInfo.expireTime = System.currentTimeMillis()+UserManager.UserCacheTime;
        
        try
		{
			userService.createOrUpdateUser(userInfo);
			UserManager.getInstance().addUser(token, userInfo);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			LogUtils.getLogger().info(e.toString());
			
			UserLoginRsp rsp = new UserLoginRsp();
        	rsp.result_code = MessageCode.DB_ERROR;
        	rsp.msg  = "account update error.";
        	rsp.token = "";
        	
        	return JSONObject.toJSONString(rsp);
		}
        
        UserLoginRsp loginRsp = new UserLoginRsp();
        loginRsp.result_code = MessageCode.SUCCESS;
        loginRsp.token = token;
        
		return JSONObject.toJSONString(loginRsp);
	}
	
	private String generateToken()
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
