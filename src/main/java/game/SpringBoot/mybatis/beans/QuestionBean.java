package game.SpringBoot.mybatis.beans;

import java.io.Serializable;

import game.SpringBoot.model.QuestionDetail;

public class QuestionBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int subjectId;
	private int questionIndex;
	private int score;
	private String content;
	private int answerType;
	private int answerCount;
	private String answers;
	private String weights;
	private String nextQuestions;
	private int analizer;
	
	public int getSubjectId()
	{
		return subjectId;
	}
	public void setSubjectId(int subjectId)
	{
		this.subjectId = subjectId;
	}
	public int getQuestionIndex()
	{
		return questionIndex;
	}
	public void setQuestionIndex(int questionIndex)
	{
		this.questionIndex = questionIndex;
	}
	public int getScore()
	{
		return score;
	}
	public void setScore(int score)
	{
		this.score = score;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content = content;
	}
	public int getAnswerType()
	{
		return answerType;
	}
	public void setAnswerType(int answerType)
	{
		this.answerType = answerType;
	}
	public int getAnswerCount()
	{
		return answerCount;
	}
	public void setAnswerCount(int answerCount)
	{
		this.answerCount = answerCount;
	}
	public String getAnswers()
	{
		return answers;
	}
	public void setAnswers(String answers)
	{
		this.answers = answers;
	}
	public String getWeights()
	{
		return weights;
	}
	public void setWeights(String weights)
	{
		this.weights = weights;
	}
	public String getNextQuestions()
	{
		return nextQuestions;
	}
	public void setNextQuestions(String nextQuestions)
	{
		this.nextQuestions = nextQuestions;
	}
	public int getAnalizer()
	{
		return analizer;
	}
	public void setAnalizer(int analizer)
	{
		this.analizer = analizer;
	}
	
	public QuestionDetail convertQuestionDetail()
	{
		QuestionDetail questionDetail= new QuestionDetail();
		questionDetail.questionIndex = this.questionIndex;
		questionDetail.score         = this.score;
		questionDetail.content       = this.content;
		questionDetail.answerType    = this.answerType;
		questionDetail.answerCount   = this.answerCount;
		questionDetail.setAnswers(this.answers);
		questionDetail.setWeights(this.weights);
		questionDetail.setNextQuestions(this.nextQuestions);
		questionDetail.analizer      = this.analizer;
		
		return questionDetail;
	}
}
