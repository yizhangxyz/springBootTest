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
import game.SpringBoot.message.MessageCode;
import game.SpringBoot.model.DrawResult;
import game.SpringBoot.model.UserInfo;

@Component
public class DrawHandler
{
	//求签信息
	static class DrawInfo
	{
		int drawIndex;       //求签序号
		int resultIndex;     //解签结果序号
		int grailCount;      //圣杯次数
	}
	
	private AtomicInteger atomicInteger = new AtomicInteger(0);

	private ConcurrentHashMap<String, DrawInfo> userDrawMap = new ConcurrentHashMap<>();
	
	//求签
	public String onDraw(UserInfo userInfo,String msgData)
	{
		//预先生成结果
		int resultIndex = getResultIndex();
		if(resultIndex < 0)
		{
			DrawRsp drawRsp    = new DrawRsp();
			drawRsp.resultCode = MessageCode.FAILED;
			drawRsp.msg        = "no result";
			
			return JSONObject.toJSONString(drawRsp);
		}
		int drawIndex = atomicInteger.incrementAndGet();
		
		DrawInfo drawInfo    = new DrawInfo();
		drawInfo.drawIndex   = drawIndex;
		drawInfo.resultIndex = resultIndex;
		drawInfo.grailCount  = 0;
		
		userDrawMap.put(userInfo.openid, drawInfo);
		
		List<DrawResult> resultList = DrawManager.getInstance().getResultList();
		
		DrawRsp drawRsp    = new DrawRsp();
		drawRsp.resultCode = MessageCode.SUCCESS;
		drawRsp.drawIndex  = drawIndex;
		drawRsp.drawType   = resultList.get(resultIndex).typeValue;
		drawRsp.drawMsg    = "";
		
		return JSONObject.toJSONString(drawRsp);
    }
	
	//投掷圣杯
	public String onThrowGrail(UserInfo userInfo,String msgData)
	{
		DrawInfo drawInfo = userDrawMap.get(userInfo.openid);
		if(drawInfo == null)
		{
			ThrowCupRsp throwCupRsp = new ThrowCupRsp();
			throwCupRsp.resultCode = MessageCode.FAILED;
			throwCupRsp.msg        = "no draw info";
			
			return JSONObject.toJSONString(throwCupRsp); 
		}
		
		boolean success = true;
		if(drawInfo.grailCount < 3)
		{
			int num = new Random().nextInt(100);
			if(num < 15)
			{
				success = false;
			}
			
			if(success)
			{
				drawInfo.grailCount++;
			}
			else
			{
				//失败就重新求签
				userDrawMap.remove(userInfo.openid);
			}
		}
		
		ThrowCupRsp throwCupRsp = new ThrowCupRsp();
		throwCupRsp.resultCode = MessageCode.SUCCESS;
		throwCupRsp.isValid    = success;
		throwCupRsp.times      = drawInfo.grailCount;
		
		return JSONObject.toJSONString(throwCupRsp);
	}
	
	//解签
	public String onAnswerTheDraw(UserInfo userInfo,String msgData)
	{
		DrawInfo drawInfo = userDrawMap.get(userInfo.openid);
		if(drawInfo == null)
		{
			DrawResultRsp rsp = new DrawResultRsp();
			rsp.resultCode = MessageCode.FAILED;
			rsp.msg        = "no draw info";

			return JSONObject.toJSONString(rsp);
		}
		
		if(drawInfo.grailCount < 3)
		{
			DrawResultRsp rsp = new DrawResultRsp();
			rsp.resultCode = MessageCode.FAILED;
			rsp.msg        = "grailCount < 3";

			return JSONObject.toJSONString(rsp);
		}

		DrawResult result = DrawManager.getInstance().getResultList().get(drawInfo.resultIndex);
		
		DrawResultRsp rsp = new DrawResultRsp();
		rsp.resultCode = MessageCode.SUCCESS;
		rsp.title      = result.title;
		rsp.type       = result.type;
		rsp.typeValue  = result.typeValue;
		rsp.verse      = result.verse;
		rsp.lotInfo    = result.lotInfo;
		rsp.answer     = result.answer;
		rsp.lucky      = result.lucky;
		rsp.story      = result.story;
		rsp.imgUrl     = result.imgUrl;

		return JSONObject.toJSONString(rsp);
    }
	
	private int getResultIndex()
	{
		List<DrawResult> resultList = DrawManager.getInstance().getResultList();
		if(resultList.size() == 0)
		{
			return -1;
		}

		return new Random().nextInt(resultList.size());
	}
}
