package game.SpringBoot.model;

public class QuestionDetail
{
	public int questionIndex;
	public int score;
	public String content;
	public int answerType;
	public int answerCount;
	public String[] answers = new String[10];
	public int[] weights = new int[10];
	public int[] nextQuestions = new int[10];
	public int analizer;

	
	public void setAnswers(String answersStr)
	{
		 String[] sArray= answersStr.split("\\*");
		 for(int i=0;i<answerCount;i++)
		 {
			 answers[i] = sArray[i];
		 }
	}
	
	public String getAnswers()
	{
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<answerCount;i++)
		{
			builder.append(answers[i]);
			builder.append("*");
		}
		return builder.toString();
	}

	public void setWeights(String weightsStr)
	{
		 String[] sArray= weightsStr.split("\\*");
		 for(int i=0;i<answerCount;i++)
		 {
			 weights[i] = Integer.parseInt(sArray[i]);
		 }
		 
	}
	
	public String getWeights()
	{
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<answerCount;i++)
		{
			builder.append(weights[i]);
			builder.append("*");
		}
		return builder.toString();
	}
	
	public void setNextQuestions(String questionsStr)
	{
		 String[] sArray= questionsStr.split("\\*");
		 for(int i=0;i<answerCount;i++)
		 {
			 nextQuestions[i] = Integer.parseInt(sArray[i]);
		 }
		 
	}
	
	public String getNextQuestions()
	{
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<answerCount;i++)
		{
			builder.append(nextQuestions[i]);
			builder.append("*");
		}
		return builder.toString();
	}
}
