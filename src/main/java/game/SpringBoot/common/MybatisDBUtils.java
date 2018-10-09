package game.SpringBoot.common;

import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MybatisDBUtils
{
	public static SqlSessionFactory sessionFactory;
    
    static
    {
        try 
        {
        	//String filePath = System.getProperty("user.dir") +"\\config\\mybatis.cfg.xml";
        	String filePath = "mybatis.cfg.xml";
        	Reader reader = Resources.getResourceAsReader(filePath);
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            LogUtils.getLogger().error(e.getMessage());
        }
        
    }
    
    //sqlsession执行sql文件
    public static SqlSession getSession()
    {
        return sessionFactory.openSession();
    }
}
