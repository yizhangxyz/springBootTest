package game.SpringBoot.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.model.DrawResult;

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

	private List<DrawResult> resultList = new ArrayList<>();
	
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
				DrawResult result = JSONObject.parseObject(str, DrawResult.class);
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

	public List<DrawResult> getResultList()
	{
		return resultList;
	}

	public void setResultList(List<DrawResult> resultList)
	{
		this.resultList = resultList;
	}
}
