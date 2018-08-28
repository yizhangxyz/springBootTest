package game.SpringBoot.analizer;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.message.ClientMessages.Answer;
import game.SpringBoot.message.ClientMessages.PostAnswers;
import game.SpringBoot.model.QuestionDetail;
import game.SpringBoot.model.Questions;

public class QuestionAnalizer
{
	public static int analize(PostAnswers userAnswers,Questions questions)
	{
		if(userAnswers.answers.size() > questions.getQuestionDetails().size())
		{
			LogUtils.getLogger().info("answer count="+userAnswers.answers+" question count="+questions.getQuestionDetails().size());
			return 0;
		}
		
		int score = 0;
		for(Answer answer : userAnswers.answers)
		{
			QuestionDetail questionDetail = questions.findQuestionDetail(answer.questionIndex);
			if(questionDetail != null)
			{
				score += analizeSubject(questionDetail,answer.answer);
			}
		}
		return score;
	}
	
	
	private static int analizeSubject(QuestionDetail subject,String answer)
	{
		Analizer analizer = Analizer.getAnalizer(subject.analizer);
		if(analizer != null)
		{
			return analizer.analize(subject, answer);
		}
		return 0;
	}
}
