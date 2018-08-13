package game.SpringBoot.model;

public class SubjectDetail
{
	public int id;
	public int subject_id;
	public int subject_index;
	public int score;
	public String content;
	public int answer_type;
	public String[] answers = new String[4];
	public int[] weights = new int[4];
	public int analizer;
}
