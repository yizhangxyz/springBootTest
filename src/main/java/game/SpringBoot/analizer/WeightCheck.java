package game.SpringBoot.analizer;

import game.SpringBoot.model.SubjectDetail;

public class WeightCheck extends Analizer
{
	private static final int TOTAL_WEIGHT = 100;

	@Override public int analize(SubjectDetail subject, String answer)
	{
		int value = Integer.parseInt(answer);
		byte bits[] = Analizer.getBitsArray(value);
		int totalWeight = 0;
		for(int i=0;i<4;i++)
		{
			if(bits[i] == 1)
			{
				totalWeight += subject.weights[i];
			}
		}
		
		float percent = (float)totalWeight/TOTAL_WEIGHT;
		return (int)(percent*subject.score);
	}

}
