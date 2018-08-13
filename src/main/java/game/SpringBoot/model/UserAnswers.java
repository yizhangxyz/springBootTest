package game.SpringBoot.model;

import java.util.List;

import game.SpringBoot.message.ClientMessages.PostAnswers;

public class UserAnswers
{
	private List<Integer> answers;

	public UserAnswers(PostAnswers postAnswers)
	{
		this.answers = postAnswers.answers;
	}
	
	public List<Integer> getAnswers()
	{
		return answers;
	}

	public void setAnswers(List<Integer> answers)
	{
		this.answers = answers;
	}
}
