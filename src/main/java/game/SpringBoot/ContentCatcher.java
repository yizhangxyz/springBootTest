package game.SpringBoot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.MyHttpClient;
import game.SpringBoot.manager.DrawManager;
import game.SpringBoot.message.ClientMessages.DrawResultRsp;

public class ContentCatcher
{
	static DrawResultRsp ParseContent(String content)
	{
		
		DrawResultRsp rsp = new DrawResultRsp();
		
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
		String urlTemplate = "xxxx";
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
			
			DrawResultRsp rsp = ParseContent(ret);
			
			System.out.println(JSONObject.toJSONString(rsp));
		}
	}
	
	static void showData()
	{
		DrawManager.getInstance().loadContent();
	}
	
	public static void main(String[] args)
	{
		//catchContent();
		showData();
	}
	
}
