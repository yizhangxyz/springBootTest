package game.SpringBoot.analizer;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.model.Questions;
import game.SpringBoot.model.SubjectDetail;
import game.SpringBoot.model.UserAnswers;

public class QuestionAnalizer
{
	private UserAnswers userAnswers;
	private Questions questions;
	
	public QuestionAnalizer(UserAnswers userAnswers,Questions questions)
	{
		this.userAnswers = userAnswers;
		this.questions = questions;
	}
	
	public int analize()
	{
		if(userAnswers.getAnswers().size() != questions.getSubjects().size())
		{
			LogUtils.getLogger().info("answer count="+userAnswers.getAnswers()+" question count="+questions.getSubjects().size());
			return 0;
		}
		
		int score = 0;
		for(int i=0;i<userAnswers.getAnswers().size();i++)
		{
			score += analizeSubject(questions.getSubjects().get(i),userAnswers.getAnswers().get(i));
		}
		return score;
	}
	
	int analizeSubject(SubjectDetail subject,Integer answer)
	{
		Analizer analizer = Analizer.getAnalizer(subject.analizer);
		if(analizer != null)
		{
			return analizer.analize(subject, answer);
		}
		return 0;
	}
}
