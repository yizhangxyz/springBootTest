package game.SpringBoot.services;

import java.sql.SQLException;
import java.util.List;

import game.SpringBoot.dao.SubjectDao;
import game.SpringBoot.model.QuestionResult;
import game.SpringBoot.model.Questions;

public class QuestionService
{
	private SubjectDao subjectDao = new SubjectDao();
	
	public Questions findByQuestionId(int id) throws SQLException
	{
		Questions questions = subjectDao.findQuestion(id);
		
		if(questions.getQuestionDetails().size() == 0)
		{
			return null;
		}
		
		List<QuestionResult> results = subjectDao.findQuestionResult(id);
		
		questions.setQuestionResults(results);
		
		return questions;
	}
}
