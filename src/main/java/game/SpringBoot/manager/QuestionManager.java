package game.SpringBoot.manager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.model.QuestionConfig;
import game.SpringBoot.model.QuestionDetail;
import game.SpringBoot.model.QuestionResult;
import game.SpringBoot.model.Questions;

public class QuestionManager
{
	static class QuestionManagerHolder
	{
		static final QuestionManager instance = new QuestionManager();
	}
	
	private Map<Integer, Questions> questionsMapOld = new HashMap<>();
	
	private Map<Integer, Questions> questionsMap = new HashMap<>();

	public static QuestionManager getInstance()
	{
		return QuestionManagerHolder.instance;
	}
	
	private int questionsVersion = 0;
	
	private Timer updateTimer = null;
	
	private QuestionManager()
	{
		updateTimer = new Timer();
		updateTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				QuestionManager.getInstance().loadAllQuestions();
			}
		}, 1*60000, 1*60000);
	}
	
	static
	{
		QuestionManager.getInstance().loadAllQuestions();
	}
	
	public void loadAllQuestions()
    {
    	BufferedInputStream inputStream = null; 
    	String proFilePath = System.getProperty("user.dir") +"\\config\\question_config.properties";  
        try
        {  
            inputStream = new BufferedInputStream(new FileInputStream(proFilePath));  
            ResourceBundle rb = new PropertyResourceBundle(inputStream);  

            int version   = Integer.parseInt(rb.getString("version"));
            if(version <= questionsVersion)
            {
            	return;
            }
            questionsVersion = version;
            
            String questions = rb.getString("questions");
            LogUtils.getLogger().info("load questions version="+version+",questions="+questions);
            
            Map<Integer, Questions> questionsTemp = new HashMap<>();
            
            String[] ids = questions.split(",");
            for(String id : ids)
            {
            	loadQuestions(questionsTemp,Integer.parseInt(id));
            }
            
            synchronized (questionsMap)
			{
            	questionsMapOld = questionsMap;
                questionsMap = questionsTemp;
			}
        } 
        catch (Exception e) 
        {  
            LogUtils.getLogger().info(e.getMessage());
        } 
        finally
        {
        	if(inputStream != null)
        	{
        		try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					LogUtils.getLogger().info(e.getMessage());
				}
        	}
		}
    }
	
	public Questions getRandomQuestion()
	{
		Questions questions = null;
		
		synchronized (questionsMap)
		{
			Object[] allQuestions = questionsMap.values().toArray();
			if(allQuestions.length > 0)
			{
				questions = (Questions)allQuestions[new Random().nextInt(allQuestions.length)];
			}
		}
		
		return questions;
	}
	
	public Questions getQuestion(int id)
	{
		Questions questions = null;
		
		synchronized (questionsMap)
		{
			questions = questionsMap.get(id);
			if(questions == null)
			{
				questions = questionsMapOld.get(id);
			}
		}
		
		return questions;
	}
	
	private void loadQuestions(Map<Integer, Questions> map,int questionId)
	{
		Questions questions = new Questions();
		try
		{
			questions.setQuestionId(questionId);
			
			loadQuestionDetails(questions,questionId);
			loadQuestionResults(questions,questionId);
			loadQuestionConfig(questions,questionId);
			
			map.put(questionId, questions);
			
			LogUtils.getLogger().info("loadQuestions "+questionId + " success!");
		}
		catch (IOException e)
		{
			LogUtils.getLogger().info(e.getMessage());
		}
	}
	
	private void loadQuestionDetails(Questions questions,int questionId) throws IOException
	{
		String filePath = System.getProperty("user.dir") +"\\game_data\\questions_"+questionId+".txt";  
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		BufferedReader bf = new BufferedReader(isr);
		String str;
		
		while ((str = bf.readLine()) != null) 
		{
			QuestionDetail questionDetail = JSONObject.parseObject(str, QuestionDetail.class);
			questions.getQuestionDetails().add(questionDetail);
		}
		
		bf.close();
		isr.close();
	}
	
	private void loadQuestionResults(Questions questions,int questionId) throws IOException
	{
		String filePath = System.getProperty("user.dir") +"\\game_data\\questions_results_"+questionId+".txt";  
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		BufferedReader bf = new BufferedReader(isr);
		String str;
		
		while ((str = bf.readLine()) != null) 
		{
			QuestionResult questionResult = JSONObject.parseObject(str, QuestionResult.class);
			questions.getQuestionResults().add(questionResult);
		}
		
		bf.close();
		isr.close();
	}
	
	private void loadQuestionConfig(Questions questions,int questionId) throws IOException
	{
		String filePath = System.getProperty("user.dir") +"\\game_data\\questions_config_"+questionId+".txt";  
		
		InputStreamReader isr = new InputStreamReader(new FileInputStream(filePath), "UTF-8");
		BufferedReader bf = new BufferedReader(isr);
		String str = bf.readLine();
		
		if(str != null)
		{
			QuestionConfig questionConfig = JSONObject.parseObject(str, QuestionConfig.class);
			questions.setQuestionConfig(questionConfig);
		}
		else
		{
			LogUtils.getLogger().info("questions "+questionId + " not config!");
		}
		bf.close();
		isr.close();
	}
}
