package game.SpringBoot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.model.QuestionDetail;
import game.SpringBoot.model.QuestionResult;
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
    
	public void importQuestionData(List<List<String>> excelList,int questionId)
	{
		Map<String, Integer> titleIndexMap = new HashMap<>();
		List<String> tileList = excelList.get(0);
    	for (int i = 0; i < tileList.size(); i++)
    	{
    		titleIndexMap.put(tileList.get(i), i);
    	}
    	
    	int questionIndex      = titleIndexMap.get("question_index");
    	int scoreIndex         = titleIndexMap.get("score");
    	int answerTypeIndex    = titleIndexMap.get("answer_type");
    	int answerCountIndex   = titleIndexMap.get("answer_count");
    	int analizerIndex      = titleIndexMap.get("analizer");
    	
    	int contentIndex       = titleIndexMap.get("content");
    	int answersIndex       = titleIndexMap.get("answers");
    	int weightsIndex       = titleIndexMap.get("weights");
    	int nextQuestionsIndex = titleIndexMap.get("next_questions");

    	
    	StringBuilder buffer = new StringBuilder();
    	int successCount = 0;
		for (int i = 1; i < excelList.size(); i++)
	    {
			QuestionDetail detail = new QuestionDetail();
	    	List<String> list = excelList.get(i);
	    	
	    	
	    	detail.questionIndex = Integer.parseInt( list.get(questionIndex));
	    	detail.score         = Integer.parseInt( list.get(scoreIndex));
	    	detail.answerType    = Integer.parseInt( list.get(answerTypeIndex));
	    	detail.answerCount  = Integer.parseInt( list.get(answerCountIndex));
	    	detail.analizer      = Integer.parseInt( list.get(analizerIndex));
	    	detail.content       = list.get(contentIndex);
	    	
	    	detail.setAnswers(list.get(answersIndex));
	    	detail.setWeights(list.get(weightsIndex));
	    	detail.setNextQuestions(list.get(nextQuestionsIndex));

	    	buffer.append(JSONObject.toJSONString(detail)+"\n");
	    	
			successCount++;
	    }
		try 
    	{
        	FileWriter writer=new FileWriter("game_data/questions_"+questionId+".txt");
        	writer.write(buffer.toString());
        	writer.close();
        }
        catch (IOException e)
        {
        	System.out.println(e.getMessage());
        }
		System.out.println(buffer.toString());
		System.out.println("create questions success, count="+successCount);
	}
	
	public void importResultData(List<List<String>> excelList,int questionId)
	{
		Map<String, Integer> titleIndexMap = new HashMap<>();
		List<String> tileList = excelList.get(0);
    	for (int i = 0; i < tileList.size(); i++)
    	{
    		titleIndexMap.put(tileList.get(i), i);
    	}
    	
    	
    	int minScoreIndex   = titleIndexMap.get("min_score");
    	int maxScoreIndex   = titleIndexMap.get("max_score");
    	int resultIndex     = titleIndexMap.get("result");

    	StringBuilder buffer = new StringBuilder();
    	int successCount = 0;
		for (int i = 1; i < excelList.size(); i++)
	    {
			QuestionResult questionResult = new QuestionResult();
	    	List<String> list = excelList.get(i);
	    	
	    	questionResult.minScore     = Integer.parseInt( list.get(minScoreIndex));
	    	questionResult.maxScore     = Integer.parseInt( list.get(maxScoreIndex));
	    	questionResult.result       = list.get(resultIndex);
	    	
	    	buffer.append(JSONObject.toJSONString(questionResult)+"\n");
	    	
	    	successCount++;
	    }
		try 
    	{
        	FileWriter writer=new FileWriter("game_data/questions_results_"+questionId+".txt");
        	writer.write(buffer.toString());
        	writer.close();
        }
        catch (IOException e)
        {
        	System.out.println(e.getMessage());
        }
		System.out.println(buffer.toString());
		System.out.println("create questionResult success, count="+successCount);
	}
	
	public static void main(String[] args)
	{
		DataImporter dataImporter = new DataImporter();
		
		int questionId = 1;
		List<List<String>> questionList = dataImporter.readExcel("game_data/questions_"+questionId+".xls",0);
	    dataImporter.importQuestionData(questionList,questionId);
	    
	    List<List<String>> resultList = dataImporter.readExcel("game_data/questions_"+questionId+".xls",1);
	    dataImporter.importResultData(resultList,questionId);
	}
}
