package game.SpringBoot.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
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
		String openId;       //所属用户
		int drawIndex;       //求签序号
		int resultIndex;     //解签结果序号
		int grailCount;      //圣杯次数
		int throwCount;      //投掷次数
		boolean finished;    //是否结束
		long createTime;     //创建时间
		long finishTime;     //结束时间
		int finishDay;       //结束日期，冗余数据，避免重复计算
		
	}
	
	private AtomicInteger atomicInteger = new AtomicInteger(0);

	private ConcurrentHashMap<String, DrawInfo> userDrawMap = new ConcurrentHashMap<>();
	
	private Timer checkTimer = null;
	
	public DrawHandler()
	{
		checkTimer = new Timer();
		checkTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				DrawHandler.this.checkDrawInfo();
			}
		}, 5*60000, 5*60000);
	}
	
	private void checkDrawInfo()
	{
		List<String> list = new ArrayList<String>();
		
		long currentTime = System.currentTimeMillis();
		int currentDay   = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
		
		for(DrawInfo drawInfo : userDrawMap.values())
		{	
			//10分钟都未结束
			if(!drawInfo.finished && (currentTime - drawInfo.createTime) > 10*60000 )
			{
				list.add(drawInfo.openId);
			}
			//已经结束，当日存储
			else if(drawInfo.finished && currentDay != drawInfo.finishDay)
			{
				list.add(drawInfo.openId);
			}
		}
		for(String id : list)
		{
			userDrawMap.remove(id);
		}
		LogUtils.getLogger().info("cache drawInfo count="+userDrawMap.size());
	}
	
	//求签
	public String onDraw(UserInfo userInfo,String msgData)
	{
		//预先生成结果
		int resultIndex = getResultIndex();
		if(resultIndex < 0)
		{
			LogUtils.getLogger().info("onDraw resultIndex="+resultIndex);
			
			DrawRsp drawRsp    = new DrawRsp();
			drawRsp.resultCode = MessageCode.FAILED;
			drawRsp.msg        = "no result";
			
			return JSONObject.toJSONString(drawRsp);
		}
		int drawIndex = atomicInteger.incrementAndGet();
		
		DrawInfo drawInfo    = new DrawInfo();
		drawInfo.openId      = userInfo.openid;
		drawInfo.drawIndex   = drawIndex;
		drawInfo.resultIndex = resultIndex;
		drawInfo.grailCount  = 0;
		drawInfo.throwCount  = 0;
		drawInfo.finished    = false;
		drawInfo.createTime  = System.currentTimeMillis();
		
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
			LogUtils.getLogger().info("onThrowGrail no draw info");
			
			ThrowCupRsp throwCupRsp = new ThrowCupRsp();
			throwCupRsp.resultCode = MessageCode.FAILED;
			throwCupRsp.msg        = "no draw info";
			
			return JSONObject.toJSONString(throwCupRsp); 
		}
		
		int throwType = 1;
		if(drawInfo.grailCount < 3)
		{
			int num = new Random().nextInt(100);
			if(num < 40)
			{
				throwType = 1;
			}
			else if(num < 85)
			{
				throwType = 2;
			}
			else if(num < 92)
			{
				throwType = 3;
			}
			else
			{
				throwType = 4;
			}
			
			drawInfo.throwCount++;
			//1、2就是是圣杯
			if(throwType == 1 || throwType == 2)
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
		throwCupRsp.checkType  = throwType;
		throwCupRsp.times      = drawInfo.throwCount;
		
		return JSONObject.toJSONString(throwCupRsp);
	}
	
	//解签
	public String onAnswerTheDraw(UserInfo userInfo,String msgData)
	{
		DrawInfo drawInfo = userDrawMap.get(userInfo.openid);
		if(drawInfo == null)
		{
			LogUtils.getLogger().info("onAnswerTheDraw no draw info");
			
			DrawResultRsp rsp = new DrawResultRsp();
			rsp.resultCode = MessageCode.FAILED;
			rsp.msg        = "no draw info";

			return JSONObject.toJSONString(rsp);
		}
		
		if(drawInfo.grailCount < 3)
		{
			LogUtils.getLogger().info("onAnswerTheDraw grailCount < 3");
			
			DrawResultRsp rsp = new DrawResultRsp();
			rsp.resultCode = MessageCode.FAILED;
			rsp.msg        = "grailCount < 3";

			return JSONObject.toJSONString(rsp);
		}
		drawInfo.finished    = true;
		drawInfo.finishTime  = System.currentTimeMillis();
		//冗余，避免不停的计算
		drawInfo.finishDay   = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

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
