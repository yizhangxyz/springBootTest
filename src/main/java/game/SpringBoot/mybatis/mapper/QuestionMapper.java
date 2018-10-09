package game.SpringBoot.mybatis.mapper;

import java.util.List;

import game.SpringBoot.mybatis.beans.QuestionBean;

public interface QuestionMapper {
   
    public int insertQuestion(QuestionBean question) throws Exception;
    
    
    public  List<QuestionBean> findQuestionById(int subjectId) throws Exception;
}