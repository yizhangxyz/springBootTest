package game.SpringBoot.controller;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.manager.DrawManager;
import game.SpringBoot.message.ClientMessages.DrawResultRsp;
import game.SpringBoot.message.ClientMessages.DrawRsp;
import game.SpringBoot.message.ClientMessages.ThrowCupRsp;
import game.SpringBoot.model.UserInfo;

@Component
public class DrawHandler
{
	//求签信息
	static class DrawInfo
	{
		int index;           //求签序号
		String description;  //描述
		int throwCount;      //投掷次数
		int grailCount;      //圣杯次数
	}
	
	private AtomicInteger atomicInteger = new AtomicInteger(0);

	private ConcurrentHashMap<String, DrawInfo> userDrawMap = new ConcurrentHashMap<>();
	
	//求签
	public String onDraw(UserInfo userInfo,String msgData)
	{
		DrawInfo drawInfo = new DrawInfo();
		drawInfo.index = atomicInteger.incrementAndGet();
		drawInfo.description = "tips";
		drawInfo.throwCount = 0;
		drawInfo.grailCount = 0;
		
		userDrawMap.put(userInfo.openid, drawInfo);
		
		DrawRsp drawRsp = new DrawRsp();
		drawRsp.resultIndex = drawInfo.index;
		drawRsp.resultMsg   = drawInfo.description;
		
		return JSONObject.toJSONString(drawRsp);
    }
	
	//投掷圣杯
	public String onThrowGrail(UserInfo userInfo,String msgData)
	{
		DrawInfo drawInfo = userDrawMap.get(userInfo.openid);
		if(drawInfo == null)
		{
			return ""; 
		}
		
		boolean success = true;
		if(drawInfo.grailCount < 3)
		{
			int num = new Random().nextInt(100);
			if(num < 40)
			{
				success = false;
			}
			
			if(success)
			{
				drawInfo.grailCount++;
			}
			drawInfo.throwCount++;
		}
		
		ThrowCupRsp throwCupRsp = new ThrowCupRsp();
		throwCupRsp.isValid    = success;
		throwCupRsp.validTimes = drawInfo.grailCount;
		throwCupRsp.times      = drawInfo.throwCount;
		
		return JSONObject.toJSONString(throwCupRsp);
	}
	
	//解签
	public String onAnswerTheDraw(UserInfo userInfo,String msgData)
	{
		DrawInfo drawInfo = userDrawMap.get(userInfo.openid);
		if(drawInfo == null)
		{
			return ""; 
		}
		
		List<DrawResultRsp> resultList = DrawManager.getInstance().getResultList();
		if(resultList.size() == 0)
		{
			return "";
		}

		int index = new Random().nextInt(resultList.size());
		
		return JSONObject.toJSONString(resultList.get(index));
    }
}
