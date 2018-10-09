package game.SpringBoot.services;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.common.MybatisDBUtils;
import game.SpringBoot.model.Questions;
import game.SpringBoot.mybatis.beans.QuestionBean;
import game.SpringBoot.mybatis.mapper.QuestionMapper;

public class QuestionService
{
	
	public Questions findByQuestionId(int id)
	{
		Questions questions = new Questions();
		
		SqlSession session = MybatisDBUtils.getSession();
        QuestionMapper mapper= session.getMapper(QuestionMapper.class);
        try 
        {
        	List<QuestionBean> questionBeanList = mapper.findQuestionById(id);
        	for(QuestionBean questionBean : questionBeanList)
        	{
        		questions.getQuestionDetails().add(questionBean.convertQuestionDetail());
        	}
        	
        	//findQuestionResult
        	
            session.commit();
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
        	LogUtils.getLogger().error(e.getMessage());
            session.rollback();
        }
		return questions;
	}
	
	public static void main(String[] args)
	{
		QuestionService questionService = new QuestionService();
		Questions questions = questionService.findByQuestionId(1);
		
		System.out.println(JSONObject.toJSONString(questions.getQuestionsView()));
	}
}
