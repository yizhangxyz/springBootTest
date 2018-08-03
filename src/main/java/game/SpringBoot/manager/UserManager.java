package game.SpringBoot.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import game.SpringBoot.model.UserInfo;

public class UserManager
{
	static class UserManagerHolder
	{
		static final UserManager instance = new UserManager();
	}
	
	private Timer validateTimer = null;
	
	private static final long UserAliveTime = 60000*60*24*30;
	
	private ConcurrentHashMap<String, UserInfo> userMap = new ConcurrentHashMap<>();
	
	public static UserManager getInstance()
	{
		return UserManagerHolder.instance;
	}
	
	private UserManager()
	{
		validateTimer = new Timer();
		validateTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				UserManager.getInstance().checkUser();
			}
		}, 60000, 60000);
	}
	
	private void checkUser()
	{
		List<String> list = new ArrayList<String>();
		
		for(UserInfo user : userMap.values())
		{
			long currentTime = System.currentTimeMillis();
			if(user.createTime - currentTime > UserAliveTime)
			{
				list.add(user.sessionId);
			}
		}
		for(String id : list)
		{
			removeUser(id);
		}
	}
	
	public UserInfo getUser(String sessionId)
	{
		return userMap.get(sessionId);
	}
	
	public void addUser(String sessionId, UserInfo userInfo)
	{
		userMap.put(sessionId, userInfo);
	}
	
	public void removeUser(String sessionId)
	{
		userMap.remove(sessionId);
	}
}
