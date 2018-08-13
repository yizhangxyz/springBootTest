package game.SpringBoot.controller;


import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.message.ClientMessages.RequestMsgData;
import game.SpringBoot.model.UserInfo;
import game.SpringBoot.services.UserService;

@Component
public class LoginValidate
{
    @Autowired
	private UserService userService;

    public UserInfo validate(String msgData)
    {
    	RequestMsgData req = JSONObject.parseObject(msgData, RequestMsgData.class);
        
        if(req == null || req.token == null)
        {
        	LogUtils.getLogger().info("parse RequestMessage error.msgData="+msgData);
        	return null;
        }
        
        //先找缓存
        UserInfo userInfo = UserManager.getInstance().getUser(req.token);
        if(userInfo != null)
        {
        	return userInfo;
        }
        
        //从数据库查找
        try
		{
			userInfo = userService.findByToken(req.token);
		}
		catch (SQLException e)
		{
			LogUtils.getLogger().info(e.toString());
		}
        if(userInfo != null)
        {
        	userInfo.expireTime = System.currentTimeMillis()+UserManager.UserCacheTime;
        	UserManager.getInstance().addUser(req.token, userInfo);
        	return userInfo;
        }

        return null;
    }
}