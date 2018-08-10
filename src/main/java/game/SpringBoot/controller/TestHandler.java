package game.SpringBoot.controller;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.model.UserInfo;

@Component
public class TestHandler
{
	public String onTest(UserInfo userInfo,String msgData)
	{
		return JSONObject.toJSONString(userInfo);
    }
}
