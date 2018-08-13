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
import game.SpringBoot.model.SubjectDetail;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class DataImporter
{
    public List<List<String>> readExcel(String filePath)
    {
        try
        {
        	InputStream is = new FileInputStream(new File(filePath));
            Workbook wb = Workbook.getWorkbook(is);
           
            List<List<String>> outerList=new ArrayList<List<String>>();
            
            Sheet sheet = wb.getSheet(0);
         
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
	
	public void importData(List<List<String>> excelList)
	{
		Map<String, Integer> titleIndexMap = new HashMap<>();
		List<String> tileList = excelList.get(0);
    	for (int i = 0; i < tileList.size(); i++)
    	{
    		titleIndexMap.put(tileList.get(i), i);
    	}
    	
    	int idIndex         = titleIndexMap.get("id");
    	int subject_idIndex = titleIndexMap.get("subject_id");
    	int subject_index   = titleIndexMap.get("index");
    	int scoreIndex      = titleIndexMap.get("score");
    	int answer_typeIndex= titleIndexMap.get("answer_type");
    	int weight1Index    = titleIndexMap.get("weight1");
    	int weight2Index    = titleIndexMap.get("weight2");
    	int weight3Index    = titleIndexMap.get("weight3");
    	int weight4Index    = titleIndexMap.get("weight4");
    	int analizerIndex   = titleIndexMap.get("analizer");
    	int contentIndex    = titleIndexMap.get("content");
    	int answer1Index    = titleIndexMap.get("answer1");
    	int answer2Index    = titleIndexMap.get("answer2");
    	int answer3Index    = titleIndexMap.get("answer3");
    	int answer4Index    = titleIndexMap.get("answer4");
    	
    	SubjectDao subjectDao = new SubjectDao();
    	
    	int subjectCount = 0;
		for (int i = 1; i < excelList.size(); i++)
	    {
			SubjectDetail subjectDetail = new SubjectDetail();
	    	List<String> list = excelList.get(i);
	    	
	    	subjectDetail.id            = Integer.parseInt( (String)list.get(idIndex));
	    	subjectDetail.subject_id    = Integer.parseInt( (String)list.get(subject_idIndex));
	    	subjectDetail.subject_index = Integer.parseInt( (String)list.get(subject_index));
	    	subjectDetail.score         = Integer.parseInt( (String)list.get(scoreIndex));
	    	subjectDetail.answer_type   = Integer.parseInt( (String)list.get(answer_typeIndex));
	    	subjectDetail.weights[0]    = Integer.parseInt( (String)list.get(weight1Index));
	    	subjectDetail.weights[1]    = Integer.parseInt( (String)list.get(weight2Index));
	    	subjectDetail.weights[2]    = Integer.parseInt( (String)list.get(weight3Index));
	    	subjectDetail.weights[3]    = Integer.parseInt( (String)list.get(weight4Index));
	    	subjectDetail.analizer      = Integer.parseInt( (String)list.get(analizerIndex));
	    	
	    	subjectDetail.content       = (String)list.get(contentIndex);
	    	subjectDetail.answers[0]    = (String)list.get(answer1Index);
	    	subjectDetail.answers[1]    = (String)list.get(answer2Index);
	    	subjectDetail.answers[2]    = (String)list.get(answer3Index);
	    	subjectDetail.answers[3]    = (String)list.get(answer4Index);
	    	
	    	try
			{
				subjectDao.insertSubject(subjectDetail);
				subjectCount++;
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("insert subject failed, count="+subjectCount);
				return;
			}
	    }
		System.out.println("insert subject success, count="+subjectCount);
	}
	
	public static void main(String[] args)
	{
		DataImporter dataImporter = new DataImporter();
		
		List<List<String>> excelList = dataImporter.readExcel("subjects.xls");
	    dataImporter.showData(excelList);
	    //dataImporter.importData(excelList);
	}
}
