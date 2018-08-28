package game.SpringBoot.manager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.model.QuestionDetail;
import game.SpringBoot.model.QuestionResult;
import game.SpringBoot.model.Questions;

public class QuestionManager
{
	static class QuestionManagerHolder
	{
		static final QuestionManager instance = new QuestionManager();
	}
	
	private Map<Integer, Questions> questionsMap = new HashMap<>();
	

	public static QuestionManager getInstance()
	{
		return QuestionManagerHolder.instance;
	}
	
	private QuestionManager()
	{
	}
	
	static
	{
		QuestionManager.getInstance().loadQuestions(1);
	}
	
	public Questions getQuestion(int id)
	{
		return questionsMap.get(id);
	}
	
	public void loadQuestions(int questionId)
	{
		Questions questions = new Questions();
		try
		{
			loadQuestionDetails(questions,questionId);
			loadQuestionResults(questions,questionId);
			
			questionsMap.remove(questionId);
			questionsMap.put(questionId, questions);
		}
		catch (IOException e)
		{
			LogUtils.getLogger().info(e.getMessage());
		}
	}
	
	private void loadQuestionDetails(Questions questions,int questionId) throws IOException
	{
		String filePath = System.getProperty("user.dir") +"\\game_data\\questions_"+questionId+".txt";  
		
		//FileReader fr = new FileReader(filePath);
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
		
		//FileReader fr = new FileReader(filePath);
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
}
