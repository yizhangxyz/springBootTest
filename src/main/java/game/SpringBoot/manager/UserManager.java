package game.SpringBoot.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.model.UserInfo;

public class UserManager
{
	static class UserManagerHolder
	{
		static final UserManager instance = new UserManager();
	}
	
	private Timer validateTimer = null;
	
	//账号缓存时间
	public static final long UserCacheTime = 60000*60*24*1;
	
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
		}, 5*60000, 5*60000);
	}
	
	private void checkUser()
	{
		List<String> list = new ArrayList<String>();
		
		long currentTime = System.currentTimeMillis();
		
		for(UserInfo user : userMap.values())
		{	
			if(user.expireTime > currentTime)
			{
				list.add(user.token);
			}
		}
		for(String id : list)
		{
			removeUser(id);
		}
		LogUtils.getLogger().info("cache user count="+userMap.size());
	}
	
	public UserInfo getUser(String token)
	{
		return userMap.get(token);
	}
	
	public void addUser(String token, UserInfo userInfo)
	{
		userMap.put(token, userInfo);
	}
	
	public void removeUser(String sessionId)
	{
		userMap.remove(sessionId);
	}
}
