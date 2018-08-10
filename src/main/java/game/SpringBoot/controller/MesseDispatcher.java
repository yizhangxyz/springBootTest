package game.SpringBoot.controller;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import game.SpringBoot.model.UserInfo;

public class MesseDispatcher
{
	public static class MsgHandler
	{
		private Object handler;
		private Method method;
		
		public MsgHandler(Object h,String m)
		{
			handler = h;
			try
			{
				method = h.getClass().getMethod(m,UserInfo.class,String.class);
			}
			catch (Exception e)
			{
				method = null;
			}
		}
		
		public String invoke(UserInfo userInfo,String data)
		{
			if(method == null)
			{
				return "";
			}
			try
			{
				return (String)method.invoke(handler,userInfo,data);
			}
			catch (Exception e)
			{
				return "";
			}
		}
	}
	
	private Map<Integer, MsgHandler> handlers = new HashMap<>();
	
	public void registerMsg(int msgId,Object handler,String method)
	{
		MsgHandler msgHandler = new MsgHandler(handler,method);
		handlers.put(msgId, msgHandler);
	}
	
	public MsgHandler getHandler(int msgId)
	{
		return handlers.get(msgId);
	}
}
