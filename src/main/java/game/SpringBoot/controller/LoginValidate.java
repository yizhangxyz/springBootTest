package game.SpringBoot.controller;


import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.model.UserInfo;
import game.SpringBoot.services.UserService;

@Component
public class LoginValidate
{
    @Autowired
	private UserService userService;

    public UserInfo validate(String token)
    {
        //先找缓存
        UserInfo userInfo = UserManager.getInstance().getUser(token);
        if(userInfo != null)
        {
        	return userInfo;
        }
        
        //从数据库查找
        try
		{
			userInfo = userService.findByToken(token);
		}
		catch (SQLException e)
		{
			LogUtils.getLogger().info(e.toString());
		}
        if(userInfo != null)
        {
        	userInfo.expireTime = System.currentTimeMillis()+UserManager.UserCacheTime;
        	UserManager.getInstance().addUser(token, userInfo);
        	return userInfo;
        }

        return null;
    }
}