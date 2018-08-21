package game.SpringBoot.controller;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.analizer.QuestionAnalizer;
import game.SpringBoot.manager.QuestionManager;
import game.SpringBoot.message.ClientMessages.PostAnswers;
import game.SpringBoot.message.ClientMessages.QuizRsp;
import game.SpringBoot.message.MessageCode;
import game.SpringBoot.model.Questions;
import game.SpringBoot.model.UserAnswers;
import game.SpringBoot.model.UserInfo;

@Component
public class QuestionHandler
{
	public String onGetQuizInfo(UserInfo userInfo,String msgData)
	{
		Questions questions = QuestionManager.getInstance().getQuestion(1);
		return JSONObject.toJSONString(questions);
    }
	
	public String onPostAnswers(UserInfo userInfo,String msgData)
	{
		PostAnswers postAnswers = JSONObject.parseObject(msgData, PostAnswers.class);
		
		Questions questions = QuestionManager.getInstance().getQuestion(postAnswers.quiz_id);
		UserAnswers userAnswers = new UserAnswers(postAnswers);
		
		if(questions == null)
		{
			QuizRsp rsp = new QuizRsp();
			rsp.result_code = MessageCode.FAILED;
			rsp.result_msg  = "no question";

			return JSONObject.toJSONString(rsp);
		}

		
		int score = QuestionAnalizer.analize(userAnswers,questions);
		
		QuizRsp rsp = new QuizRsp();
		rsp.result_code = MessageCode.SUCCESS;
		rsp.result_msg  = questions.getResult(score);
		rsp.score = score;
		
		return JSONObject.toJSONString(rsp);
    }
}
