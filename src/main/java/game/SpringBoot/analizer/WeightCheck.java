package game.SpringBoot.analizer;

import game.SpringBoot.model.QuestionDetail;

public class WeightCheck extends Analizer
{
	private static final int TOTAL_WEIGHT = 100;

	@Override public int analize(QuestionDetail subject, String answer)
	{
		String[] sArray= answer.split("\\,");
		
		int totalWeight = 0;
		for(String s : sArray)
		{
			int answerIndex = Integer.parseInt(s);
			if(answerIndex >=1 && answerIndex <= subject.answerCount)
			{
				totalWeight += subject.weights[answerIndex-1];
			}
		}
		
		return totalWeight*subject.score/TOTAL_WEIGHT;
	}

}
