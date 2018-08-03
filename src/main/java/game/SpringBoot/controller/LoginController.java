package game.SpringBoot.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.httpUtils.MyHttpClient;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.message.ClientMessages.ErrorRsp;
import game.SpringBoot.message.ClientMessages.LoginCodeReq;
import game.SpringBoot.message.ServerMessages.LoginFailed;
import game.SpringBoot.message.ServerMessages.LoginSuccess;
import game.SpringBoot.model.UserInfo;
import game.SpringBoot.tools.LogUtils;
import game.SpringBoot.tools.PropertiesConfig;

@RestController
//@Controller
public class LoginController
{
	
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
        
        String sessionId = generateSessionId();
        
        UserInfo userInfo = new UserInfo();
        
        userInfo.sessionId = sessionId;
        userInfo.session_key = successInfo.session_key;
        userInfo.openid = successInfo.openid;
        userInfo.createTime = System.currentTimeMillis();
        
        
        UserManager.getInstance().addUser(sessionId, userInfo);
        
		return ret;
    }
	
	public String getUserData(String access_token,String openid)
	{
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token +"&openid="+openid;
        return MyHttpClient.doGet(url);       
	}
	
	private String generateSessionId()
	{
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
