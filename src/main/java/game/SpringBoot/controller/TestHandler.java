package game.SpringBoot.controller;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.model.Questions;
import game.SpringBoot.model.UserInfo;
import game.SpringBoot.services.QuestionService;

@Component
public class TestHandler
{
	public String onTest(UserInfo userInfo,String msgData)
	{
		QuestionService questionService = new QuestionService();
		Questions questions = questionService.findByQuestionId(1);
		
		return JSONObject.toJSONString(questions.getQuestionsView());
    }
}
