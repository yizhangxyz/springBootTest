package game.SpringBoot.analizer;

import java.util.HashMap;
import java.util.Map;

import game.SpringBoot.model.SubjectDetail;

public abstract class Analizer
{
	public abstract int analize(SubjectDetail subject,String answer);
	
	private static final int WEIGHT_CHECK     = 1;
	private static final int RANDOM_BY_WEIGHT = 2;
	
	private static Map<Integer, Analizer> analizersMap = new HashMap<>();
	
	static
	{
		registerAnalizer(WEIGHT_CHECK,    new WeightCheck());
		registerAnalizer(RANDOM_BY_WEIGHT,new RandomByWeight());
	}
	
	private static void registerAnalizer(int id,Analizer analizer)
	{
		analizersMap.put(id, analizer);
	}
	
	public static Analizer getAnalizer(int id)
	{
		return analizersMap.get(id);
	}
	
	public static byte[] getBitsArray(int value)
	{  
		byte[] array = new byte[8];  
		for (int i=0; i<8;i++) 
		{  
		    array[i] = (byte)(value & 1);  
		    value = (byte) (value >> 1);  
		}  
		return array;  
	}  
}
