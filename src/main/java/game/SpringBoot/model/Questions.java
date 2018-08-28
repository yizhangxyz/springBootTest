package game.SpringBoot.model;

import java.util.ArrayList;
import java.util.List;

public class Questions
{
	private List<QuestionDetail> questionDetails = new ArrayList<>();

	private List<QuestionResult> questionResults = new ArrayList<>();
	
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
	
}
