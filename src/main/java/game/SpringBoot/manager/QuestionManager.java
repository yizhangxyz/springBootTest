package game.SpringBoot.manager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.model.Questions;
import game.SpringBoot.services.QuestionService;

public class QuestionManager
{
	static class QuestionManagerHolder
	{
		static final QuestionManager instance = new QuestionManager();
	}
	
	private Map<Integer, Questions> questionsMap = new HashMap<>();
	
	private static final ReadWriteLock lock = new ReentrantReadWriteLock();
	
	private QuestionService questionService = new QuestionService();
	
	public static QuestionManager getInstance()
	{
		return QuestionManagerHolder.instance;
	}
	
	private QuestionManager()
	{
		
	}
	
	public Questions getQuestion(int id)
	{
		Questions questions = null;
		
		lock.readLock().lock();
		try
		{
			questions = questionsMap.get(id);
		}
		finally
		{
			lock.readLock().unlock();
		}
		
		if(questions != null)
		{
			return questions;
		}
		
		try
		{
			questions = questionService.findByQuestionId(id);
			if(questions != null)
			{
				lock.writeLock().lock();
				try
				{
					questionsMap.put(id,questions);
					LogUtils.getLogger().info("cache questions count="+questionsMap.size());
				}
				finally
				{
					lock.writeLock().unlock();
				}
			}
			
		}
		catch (SQLException e)
		{
			LogUtils.getLogger().info(e.toString());
		}
		return questions;
	}
}
