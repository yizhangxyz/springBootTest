package game.SpringBoot.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import game.SpringBoot.common.DBUtils;
import game.SpringBoot.model.Questions;
import game.SpringBoot.model.SubjectDetail;
import game.SpringBoot.model.UserInfo;

public class SubjectDao
{
	public void insertSubject(SubjectDetail subject) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into subjects(id,subject_id,subject_index,score,content,answer_type,answer1,answer2,answer3,answer4,weight1,weight2,weight3,weight4,analizer) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, subject.id);
            ps.setInt(2, subject.subject_id);
            ps.setInt(3, subject.subject_index);
            ps.setInt(4, subject.score);
            ps.setString(5, subject.content);
            ps.setInt(6, subject.answer_type);
            ps.setString(7, subject.answers[0]);
            ps.setString(8, subject.answers[1]);
            ps.setString(9, subject.answers[2]);
            ps.setString(10, subject.answers[3]);
            ps.setInt(11, subject.weights[0]);
            ps.setInt(12, subject.weights[1]);
            ps.setInt(13, subject.weights[2]);
            ps.setInt(14, subject.weights[3]);
            ps.setInt(15, subject.analizer);
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
        Questions questions = new Questions();
        String sql = "select id,subject_id,subject_index,score,content,answer_type,answer1,answer2,answer3,answer4,weight1,weight2,weight3,weight4,analizer"
        		+ " from subjects where subject_id=? order by subject_index ";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            
            List<SubjectDetail> subjects = new ArrayList<>();
            if(rs.next())
            {
            	SubjectDetail subjectDetail = new SubjectDetail();
            	
            	subjectDetail.id = rs.getInt(1);
            	subjectDetail.subject_id = rs.getInt(2);
            	subjectDetail.subject_index = rs.getInt(3);
            	subjectDetail.score = rs.getInt(4);
            	subjectDetail.content = rs.getString(5);
            	subjectDetail.answer_type = rs.getInt(6);
            	subjectDetail.answers[0] = rs.getString(7);
            	subjectDetail.answers[1] = rs.getString(8);
            	subjectDetail.answers[2] = rs.getString(9);
            	subjectDetail.answers[3] = rs.getString(10);
            	subjectDetail.weights[0] = rs.getInt(11);
            	subjectDetail.weights[1] = rs.getInt(12);
            	subjectDetail.weights[2] = rs.getInt(13);
            	subjectDetail.weights[3] = rs.getInt(14);
            	subjectDetail.analizer = rs.getInt(15);
            	
            	subjects.add(subjectDetail);
            }
            questions.setSubjects(subjects);
        }
        catch(SQLException e)
        {
        	throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(rs, ps, conn);
        }
        return questions;
    }
}
