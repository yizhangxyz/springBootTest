package game.SpringBoot.analizer;

import java.util.Random;

import game.SpringBoot.model.SubjectDetail;

public class RandomByWeight extends Analizer
{
	@Override public int analize(SubjectDetail subject, int answer)
	{
		byte bits[] = Analizer.getBitsArray(answer);
		
		for(int i=0;i<4;i++)
		{
			if(bits[i] == 1)
			{
				int num = new Random().nextInt(100);
				if(num < subject.weights[i])
				{
					return subject.score;
				}
			}
		}
		return 0;
	}
}
