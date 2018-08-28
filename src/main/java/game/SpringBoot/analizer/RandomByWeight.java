package game.SpringBoot.analizer;

import java.util.Random;

import game.SpringBoot.model.QuestionDetail;

public class RandomByWeight extends Analizer
{
	private static final int TOTAL_WEIGHT = 100;
	
	@Override public int analize(QuestionDetail subject, String answer)
	{
		String[] sArray= answer.split("\\,");
		
		for(String s : sArray)
		{
			int answerIndex = Integer.parseInt(s);
			if(answerIndex >=1 && answerIndex <= subject.answerCount)
			{
				int num = new Random().nextInt(TOTAL_WEIGHT);
				if(num < subject.weights[answerIndex-1])
				{
					return subject.score;
				}
			}
		}

		return 0;
	}
}
