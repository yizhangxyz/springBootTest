package game.SpringBoot.analizer;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.model.Questions;
import game.SpringBoot.model.SubjectDetail;
import game.SpringBoot.model.UserAnswers;

public class QuestionAnalizer
{
	public static int analize(UserAnswers userAnswers,Questions questions)
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
	
	
	private static int analizeSubject(SubjectDetail subject,String answer)
	{
		Analizer analizer = Analizer.getAnalizer(subject.analizer);
		if(analizer != null)
		{
			return analizer.analize(subject, answer);
		}
		return 0;
	}
}
