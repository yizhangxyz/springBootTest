package game.SpringBoot.model;

import java.util.ArrayList;
import java.util.List;

import game.SpringBoot.message.ClientMessages.QuestionView;
import game.SpringBoot.message.ClientMessages.QuestionsRsp;

public class Questions
{
	private int questionId;
	
	private QuestionConfig questionConfig;


	private List<QuestionDetail> questionDetails = new ArrayList<>();

	private List<QuestionResult> questionResults = new ArrayList<>();
	
	public int getQuestionId()
	{
		return questionId;
	}

	public void setQuestionId(int questionId)
	{
		this.questionId = questionId;
	}
	
	public QuestionConfig getQuestionConfig()
	{
		return questionConfig;
	}

	public void setQuestionConfig(QuestionConfig questionConfig)
	{
		this.questionConfig = questionConfig;
	}
	
	public QuestionDetail findQuestionDetail(int questionIndex)
	{
		for(QuestionDetail questionDetail : questionDetails)
		{
			if(questionDetail.questionIndex == questionIndex)
			{
				return questionDetail;
			}
		}
		return null;
	}
	
	public List<QuestionDetail> getQuestionDetails()
	{
		return questionDetails;
	}

	public void setQuestionDetails(List<QuestionDetail> questionDetails)
	{
		this.questionDetails = questionDetails;
	}

	public List<QuestionResult> getQuestionResults()
	{
		return questionResults;
	}

	public void setQuestionResults(List<QuestionResult> questionResults)
	{
		this.questionResults = questionResults;
	}
	
	public String getResult(int score)
	{
		for(QuestionResult questionResult : questionResults)
		{
			if(score >= questionResult.minScore && score <= questionResult.maxScore)
			{
				return questionResult.result;
			}
		}
		return "";
	}
	
	//客户端展示的题目
	public QuestionsRsp getQuestionsView()
	{
		QuestionsRsp rsp = new QuestionsRsp();
		rsp.quizId    = this.questionId;
		rsp.quizTitle = this.questionConfig.title;
		for(QuestionDetail questionDetail : questionDetails)
		{
			QuestionView questionView = new QuestionView();
			
			questionView.questionIndex = questionDetail.questionIndex;
			questionView.score         = questionDetail.score;
			questionView.content       = questionDetail.content;  
			questionView.answerType    = questionDetail.answerType;
			questionView.answerCount   = questionDetail.answerCount;
			questionView.answers       = questionDetail.getAnswers();
			questionView.nextQuestions = questionDetail.getNextQuestions();
			
			rsp.questionsView.add(questionView);
		}
		return rsp;
	}
	
}
