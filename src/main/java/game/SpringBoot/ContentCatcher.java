package game.SpringBoot;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.MyHttpClient;
import game.SpringBoot.manager.DrawManager;
import game.SpringBoot.message.ClientMessages.DrawRetType;
import game.SpringBoot.model.DrawResult;

public class ContentCatcher
{
	static DrawResult ParseContent(String content)
	{
		
		DrawResult rsp = new DrawResult();
		
    	String html  = content;
    	Document doc = Jsoup.parse(html);
    	
    	String title  =  doc.select("body").get(0).select("p").get(0).select("b").text();
    	String type   =  doc.select("body").get(0).select("p").get(1).select("b").text();
    	String verse  =  doc.select("body").get(0).select("p").get(2).ownText();
    	String lotInfo=  doc.select("body").get(0).select("p").get(3).ownText();
    	String answer =  doc.select("body").get(0).select("p").get(4).ownText();
    	String lucky  =  doc.select("body").get(0).select("p").get(5).ownText();
    	String story  =  doc.select("body").get(0).select("p").get(6).ownText();
    	
    	rsp.title = recovery(title);
    	rsp.type  = recovery(type);
    	rsp.verse = recovery(verse);
    	rsp.lotInfo = recovery(lotInfo);
    	rsp.answer = recovery(answer);
    	rsp.lucky = recovery(lucky);
    	rsp.story = recovery(story);
    	
    	return rsp;
	}
	
	static String recovery(String str)
	{
		return str.replaceAll("br2n", "\n");
	}
	
	static void catchContent()
	{
		String urlTemplate = "http://www.buyiju.com/guanyin/xx.html";
		for(int i=1;i<=100;i++)
		{
			if(i == 72)
			{
				continue;
			}
			String url = urlTemplate.replaceAll("xx", i+"");
			
			String ret = MyHttpClient.doGet(url);
			
			ret = ret.replaceAll("(?i)<br[^>]*>", "br2n");
			
			//System.out.println(ret);
			
			DrawResult rsp = ParseContent(ret);
			rsp.imgUrl = "images/image"+i+".jpg";
			String type = rsp.type.substring(3);
			if(type.equals("上签"))
			{
				rsp.typeValue = DrawRetType.Great;
			}
			else if(type.equals("中上签"))
			{
				rsp.typeValue = DrawRetType.Good;
			}
			else if(type.equals("中签"))
			{
				rsp.typeValue = DrawRetType.Normal;
			}
			else if(type.equals("中下签"))
			{
				rsp.typeValue = DrawRetType.Low;
			}
			else if(type.equals("下签"))
			{
				rsp.typeValue = DrawRetType.Bad;
			}
			else
			{
				rsp.typeValue = DrawRetType.Invalid;
				System.out.println("type->"+type);
			}
			System.out.println(JSONObject.toJSONString(rsp));
		}
	}
	
	static void showData()
	{
		DrawManager.getInstance().loadContent();
	}
	
	static void catchImg()
	{
		String urlTemplate = "http://www.buyiju.com/guanyin/img/xx.gif";
		for(int i=1;i<=100;i++)
		{
			if(i == 72)
			{
				continue;
			}
			String urlPath = urlTemplate.replaceAll("xx", i+"");
			
			URL url = null;
			
			String imageName =  "D:/wxgame_web/images/image"+i+".jpg";

	        try 
	        {
	            url = new URL(urlPath);
	            DataInputStream dataInputStream = new DataInputStream(url.openStream());

	            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
	            ByteArrayOutputStream output = new ByteArrayOutputStream();

	            byte[] buffer = new byte[1024];
	            int length;

	            while ((length = dataInputStream.read(buffer)) > 0)
	            {
	                output.write(buffer, 0, length);
	            }
	           
	            fileOutputStream.write(output.toByteArray());
	            dataInputStream.close();
	            fileOutputStream.close();
	            
	            System.out.println("load image"+i);
	        } 
	        catch (MalformedURLException e) 
	        {
	            e.printStackTrace();
	        } 
	        catch (IOException e) 
	        {
	            e.printStackTrace();
	        }
		}
	}
	
	public static void main(String[] args)
	{
		catchContent();
		//showData();
		//catchImg();
	}
	
}
