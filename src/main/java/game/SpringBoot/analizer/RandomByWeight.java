package game.SpringBoot.analizer;

import java.util.Random;

import game.SpringBoot.model.SubjectDetail;

public class RandomByWeight extends Analizer
{
	private static final int TOTAL_WEIGHT = 100;
	
	@Override public int analize(SubjectDetail subject, String answer)
	{
		int value = Integer.parseInt(answer);
		byte bits[] = Analizer.getBitsArray(value);
		
		for(int i=0;i<4;i++)
		{
			if(bits[i] == 1)
			{
				int num = new Random().nextInt(TOTAL_WEIGHT);
				if(num < subject.weights[i])
				{
					return subject.score;
				}
			}
		}
		return 0;
	}
}
