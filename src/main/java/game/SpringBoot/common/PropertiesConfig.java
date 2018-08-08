package game.SpringBoot.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesConfig
{
	public static final String ServerConfig = "config.properties";
	
	public static String readData(String filePath, String key)
	{  
        filePath = PropertiesConfig.class.getResource("/" + filePath).toString();  
        filePath = filePath.substring(6);  
        
        InputStream in = null;
        try 
        {  
        	Properties props = new Properties();  
        	
            in = new BufferedInputStream(new FileInputStream(filePath));  
            props.load(in);  
 
            return props.getProperty(key); 
        } 
        catch (Exception e)
        {  
            e.printStackTrace();  
            return "";  
        }  
        finally 
        {
        	if(in != null)
    		{
    			 try
				{
					in.close();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
    		}
		}
    }  
	
	public static void main(String[] args)
	{
		String appid = PropertiesConfig.readData(PropertiesConfig.ServerConfig, "WX_APPID");
		System.out.println("appId="+appid);
	}
}
