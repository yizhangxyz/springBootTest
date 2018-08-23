package game.SpringBoot.model;

import java.util.List;

public class Questions
{
	private List<SubjectDetail> subjects;
	
	private List<QuestionResult> questionResults;
	
	public List<SubjectDetail> getSubjects()
	{
		return subjects;
	}

	public void setSubjects(List<SubjectDetail> subjects)
	{
		this.subjects = subjects;
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
