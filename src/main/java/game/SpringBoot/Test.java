package game.SpringBoot;

import java.util.concurrent.ConcurrentHashMap;

public class Test
{

	public static void main(String[] args)
	{
		ConcurrentHashMap<String, Integer> userMap = new ConcurrentHashMap<>();
		
		userMap.put("a", 1);
		userMap.put("a", 1);
		
		System.out.println(userMap.size());
	}

}
