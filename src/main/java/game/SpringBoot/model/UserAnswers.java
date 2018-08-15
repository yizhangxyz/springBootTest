package game.SpringBoot.model;

import java.util.List;

import game.SpringBoot.message.ClientMessages.PostAnswers;

public class UserAnswers
{
	private List<String> answers;

	public UserAnswers(PostAnswers postAnswers)
	{
		this.answers = postAnswers.answers;
	}

	public List<String> getAnswers()
	{
		return answers;
	}

	public void setAnswers(List<String> answers)
	{
		this.answers = answers;
	}
}
