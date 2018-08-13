package game.SpringBoot.model;

import java.util.List;

public class Questions
{
	private List<SubjectDetail> subjects;

	public List<SubjectDetail> getSubjects()
	{
		return subjects;
	}

	public void setSubjects(List<SubjectDetail> subjects)
	{
		this.subjects = subjects;
	}
}
