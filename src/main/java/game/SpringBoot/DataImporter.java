package game.SpringBoot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.SpringBoot.dao.SubjectDao;
import game.SpringBoot.model.QuestionResult;
import game.SpringBoot.model.SubjectDetail;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DataImporter
{
    public List<List<String>> readExcel(String filePath,int sheetNum)
    {
        try
        {
        	InputStream is = new FileInputStream(new File(filePath));
            Workbook wb = Workbook.getWorkbook(is);
           
            List<List<String>> outerList=new ArrayList<List<String>>();
            
            Sheet sheet = wb.getSheet(sheetNum);
         
            for (int i = 0; i < sheet.getRows(); i++)
            {
                List<String> innerList = new ArrayList<String>();
                for (int j = 0; j < sheet.getColumns(); j++) 
                {
                    String cellinfo = sheet.getCell(j, i).getContents();
                    innerList.add(cellinfo);
                }
                outerList.add(i, innerList);
            }
            return outerList;
        } 
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } 
        catch (BiffException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
	public void showData(List<List<String>>  excelList)
	{
		System.out.println("list中的数据打印出来");
	    for (int i = 0; i < excelList.size(); i++)
	    {
	    	List<String> list = excelList.get(i);
	    	for (int j = 0; j < list.size(); j++)
	    	{
	    		System.out.print(list.get(j)+",");
	    	}
	        System.out.println();
	    }
	}
	
	public void importQuestionData(List<List<String>> excelList)
	{
		Map<String, Integer> titleIndexMap = new HashMap<>();
		List<String> tileList = excelList.get(0);
    	for (int i = 0; i < tileList.size(); i++)
    	{
    		titleIndexMap.put(tileList.get(i), i);
    	}
    	
    	int idIndex          = titleIndexMap.get("id");
    	int subject_idIndex  = titleIndexMap.get("subject_id");
    	int subject_index    = titleIndexMap.get("subject_index");
    	int scoreIndex       = titleIndexMap.get("score");
    	int answer_countIndex= titleIndexMap.get("answer_count");
    	int answer_typeIndex = titleIndexMap.get("answer_type");
    	int analizerIndex    = titleIndexMap.get("analizer");
    	
    	int contentIndex     = titleIndexMap.get("content");
    	int answersIndex     = titleIndexMap.get("answers");
    	int weightsIndex     = titleIndexMap.get("weights");
    	
    	
    	SubjectDao subjectDao = new SubjectDao();
    	
    	int successCount = 0;
		for (int i = 1; i < excelList.size(); i++)
	    {
			SubjectDetail subjectDetail = new SubjectDetail();
	    	List<String> list = excelList.get(i);
	    	
	    	subjectDetail.id            = Integer.parseInt( list.get(idIndex));
	    	subjectDetail.subject_id    = Integer.parseInt( list.get(subject_idIndex));
	    	subjectDetail.subject_index = Integer.parseInt( list.get(subject_index));
	    	subjectDetail.score         = Integer.parseInt( list.get(scoreIndex));
	    	subjectDetail.answer_type   = Integer.parseInt( list.get(answer_typeIndex));
	    	subjectDetail.answer_count  = Integer.parseInt( list.get(answer_countIndex));
	    	subjectDetail.analizer      = Integer.parseInt( list.get(analizerIndex));
	    	subjectDetail.content       = list.get(contentIndex);
	    	
	    	subjectDetail.setAnswers(list.get(answersIndex));
	    	subjectDetail.setWeights(list.get(weightsIndex));

	    	try
			{
				subjectDao.insertSubject(subjectDetail);
				successCount++;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("insert subject failed, count="+successCount);
				return;
			}
	    }
		System.out.println("insert subject success, count="+successCount);
	}
	
	public void importResultData(List<List<String>> excelList)
	{
		Map<String, Integer> titleIndexMap = new HashMap<>();
		List<String> tileList = excelList.get(0);
    	for (int i = 0; i < tileList.size(); i++)
    	{
    		titleIndexMap.put(tileList.get(i), i);
    	}
    	
    	int idIndex          = titleIndexMap.get("id");
    	int subject_idIndex  = titleIndexMap.get("subject_id");
    	int min_scoreIndex   = titleIndexMap.get("min_score");
    	int max_scoreIndex   = titleIndexMap.get("max_score");
    	int resultIndex      = titleIndexMap.get("result");

    	SubjectDao subjectDao = new SubjectDao();
    	
    	int successCount = 0;
		for (int i = 1; i < excelList.size(); i++)
	    {
			QuestionResult questionResult = new QuestionResult();
	    	List<String> list = excelList.get(i);
	    	
	    	questionResult.id            = Integer.parseInt( list.get(idIndex));
	    	questionResult.subject_id    = Integer.parseInt( list.get(subject_idIndex));
	    	questionResult.min_score     = Integer.parseInt( list.get(min_scoreIndex));
	    	questionResult.max_score     = Integer.parseInt( list.get(max_scoreIndex));
	    	questionResult.result        = list.get(resultIndex);
	    		
	    	try
			{
				subjectDao.insertQuestionResult(questionResult);
				successCount++;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("insert questionResult failed, count="+successCount);
				return;
			}
	    }
		System.out.println("insert questionResult success, count="+successCount);
	}
	
	public static void main(String[] args)
	{
		DataImporter dataImporter = new DataImporter();
		
		List<List<String>> questionList = dataImporter.readExcel("subjects.xls",0);
	    dataImporter.importQuestionData(questionList);
	    
	    List<List<String>> resultList = dataImporter.readExcel("subjects.xls",1);
	    dataImporter.importResultData(resultList);
	}
}
