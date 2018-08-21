package game.SpringBoot.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.message.ClientMessages.DrawResultRsp;
import game.SpringBoot.message.ClientMessages.LoginMsgData;

public class DrawManager
{
	static class DrawManagerHolder
	{
		static final DrawManager instance = new DrawManager();
	}
	
	public static DrawManager getInstance()
	{
		return DrawManagerHolder.instance;
	}

	private List<DrawResultRsp> resultList = new ArrayList<>();
	
	private DrawManager()
	{
	}
	
	static
	{
		DrawManager.getInstance().loadContent();
	}
	
	public void loadContent()
	{
		try
		{
			resultList.clear();
			
	        String filePath = DrawManager.class.getResource("/draw_results.txt").getPath();  

			FileReader fr = new FileReader(filePath);
			BufferedReader bf = new BufferedReader(fr);
			String str;
			
			while ((str = bf.readLine()) != null) 
			{
				DrawResultRsp result = JSONObject.parseObject(str, DrawResultRsp.class);
				resultList.add(result);
			}
			bf.close();
			fr.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public List<DrawResultRsp> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<DrawResultRsp> resultList)
	{
		this.resultList = resultList;
	}
}
