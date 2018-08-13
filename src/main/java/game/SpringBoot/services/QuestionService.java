package game.SpringBoot.services;

import java.sql.SQLException;

import game.SpringBoot.dao.SubjectDao;
import game.SpringBoot.model.Questions;

public class QuestionService
{
	private SubjectDao subjectDao = new SubjectDao();
	
	public Questions findByQuestionId(int id) throws SQLException
	{
		return subjectDao.findQuestion(id);
	}
}
