package game.SpringBoot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import game.SpringBoot.common.DBUtils;
import game.SpringBoot.model.QuestionDetail;
import game.SpringBoot.model.QuestionResult;
import game.SpringBoot.model.Questions;

public class SubjectDao
{
	public void insertSubject(QuestionDetail subject) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into subjects(id,subject_id,subject_index,score,content,answer_type,answer_count,answers,weights,analizer) values(?,?,?,?,?,?,?,?,?,?)";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(4, subject.score);
            ps.setString(5, subject.content);
            ps.setInt(6, subject.answerType);
            ps.setInt(7, subject.answerCount);
            ps.setString(8, subject.getAnswers());
            ps.setString(9, subject.getWeights());
            ps.setInt(10, subject.analizer);
            ps.execute();
        }
        catch(SQLException e)
        {
        	throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(null, ps, conn);
        }
    }
	
	public void insertQuestionResult(QuestionResult questionResult) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into subject_result(id,subject_id,min_score,max_score,result) values(?,?,?,?,?)";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(3, questionResult.minScore);
            ps.setInt(4, questionResult.maxScore);
            ps.setString(5, questionResult.result);
            ps.execute();
        }
        catch(SQLException e)
        {
        	throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(null, ps, conn);
        }
    }
	
	public Questions findQuestion(int id) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        String sql = "select id,subject_id,subject_index,score,content,answer_type,answer_count,answers,weights,analizer"
        		+ " from subjects where subject_id=? order by subject_index ";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            Questions questions = new Questions();
            
            List<QuestionDetail> questionDetails = new ArrayList<>();
            while(rs.next())
            {
            	QuestionDetail detail = new QuestionDetail();
            	

            	detail.score = rs.getInt(4);
            	detail.content = rs.getString(5);
            	detail.answerType = rs.getInt(6);
            	detail.answerCount = rs.getInt(7);
            	detail.setAnswers(rs.getString(8));
            	detail.setWeights(rs.getString(9));
            	detail.analizer = rs.getInt(10);
            	
            	questionDetails.add(detail);
            	
            }
            questions.setQuestionDetails(questionDetails);
            
            return questions;
        }
        catch(SQLException e)
        {
        	throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(rs, ps, conn);
        }
    }
	
	public List<QuestionResult> findQuestionResult(int id) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
            
        String sql = "select id,subject_id,min_score,max_score,result from subject_result where subject_id=?";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            List<QuestionResult> results = new ArrayList<>();

            while(rs.next())
            {
            	QuestionResult questionResult = new QuestionResult();

            	questionResult.minScore = rs.getInt(3);
            	questionResult.maxScore = rs.getInt(4);
            	questionResult.result = rs.getString(5);
            	
            	results.add(questionResult);
            }
            return results;
        }
        catch(SQLException e)
        {
        	throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(rs, ps, conn);
        }
    }
}
