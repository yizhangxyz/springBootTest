package game.SpringBoot.common;

import java.util.ResourceBundle;

public class PropertiesConfig
{
	public static final String ServerConfig = "config";
	private static ResourceBundle ServerConfigBundle;
	
	static
	{
		ServerConfigBundle = ResourceBundle.getBundle(ServerConfig);
	}
	
	public static String readData(String fileName, String key)
	{  
		ResourceBundle bundle = null;
		switch (fileName)
	    {
	    	case ServerConfig:
	    		bundle = ServerConfigBundle;
	       		break;

	       	default:
	       		break;
	    }
		
		if(bundle != null)
		{
			return bundle.getString(key);
		}
		LogUtils.getLogger().info("readConfig failed fileName="+fileName+" key="+key);
		return "";
    }  
	
	public static void main(String[] args)
	{
		String appid = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_APPID");
		System.out.println("appId="+appid);
	}
}
