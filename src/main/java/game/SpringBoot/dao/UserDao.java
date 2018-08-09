package game.SpringBoot.dao;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import game.SpringBoot.common.DBUtils;
import game.SpringBoot.common.MessageCode;
import game.SpringBoot.model.UserInfo;


@Component
public class UserDao{

    public void createOrUpdateUser(UserInfo userInfo) throws SQLException 
    {
        Connection conn = null;
        CallableStatement  cstm = null;
        String sql = "{CALL create_update_user(?,?,?,?)}";
        try
        {
            conn = DBUtils.getConnection();
            cstm = conn.prepareCall(sql);
            cstm.setString(1, userInfo.openid);
            cstm.setString(2, userInfo.token);
            cstm.setString(3, userInfo.session_key);
            cstm.setTimestamp(4,new Timestamp(userInfo.updateTime));

            //cstm.registerOutParameter(1, Types.INTEGER); // 设置返回值类型 即返回值 
        
            cstm.execute();
            
            //System.out.println(cstm.getInt(2));

        }
        catch(SQLException e)
        {
            throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(null, cstm, conn);
        }
    }

    public UserInfo findByToken(String token) throws SQLException 
    {
        Connection conn = null;
        CallableStatement ps = null;
        ResultSet rs = null;
        String sql = "{CALL find_user_bytoken(?)}";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareCall(sql);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if(rs.next())
            {
            	int ret = rs.getInt(1);
            	if(ret == MessageCode.SUCCESS)
            	{
            		UserInfo userInfo = new UserInfo();
                	userInfo.openid = rs.getString(2);
                	userInfo.token = rs.getString(3);
                	userInfo.session_key = rs.getString(4);
                	userInfo.createTime = rs.getTimestamp(5).getTime();
                	userInfo.updateTime = rs.getTimestamp(6).getTime();
                	
                	return userInfo;
            	}
            	else 
            	{
            		return null;
            	}
            }
        }
        catch(SQLException e)
        {
        	throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(rs, ps, conn);
        }
        return null;
    }
    
    public UserInfo findByOpenId(String openId) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserInfo userInfo = null;
        String sql = "select openId,token,sessionKey,createTime,updateTime from users where openId=?";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, openId);
            rs = ps.executeQuery();
            if(rs.next())
            {
            	userInfo = new UserInfo();
            	userInfo.openid = rs.getString(1);
            	userInfo.token = rs.getString(2);
            	userInfo.session_key = rs.getString(3);
            	userInfo.createTime = rs.getTimestamp(4).getTime();
            	userInfo.updateTime = rs.getTimestamp(5).getTime();
            }
        }
        catch(SQLException e)
        {
        	throw new SQLException(e.toString()+" sql="+sql);
        }
        finally
        {
            DBUtils.close(rs, ps, conn);
        }
        return userInfo;
    }

    public void update(UserInfo userInfo) throws SQLException
    {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "update users set token=?,sessionKey=?,updateTime=? where openId=?";
        try
        {
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userInfo.token);
            ps.setString(2, userInfo.session_key);
            ps.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            ps.setString(4, userInfo.openid);
            
            ps.executeUpdate();
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

    /*
     * 
     * 
    public void delete(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "delete from person where id=?";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new SQLException(" 删除数据失败");
        }finally{
            DBUtils.close(null, ps, conn);
        }        
    }
    public List<Person> findAll() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Person p = null;
        List<Person> persons = new ArrayList<Person>();
        String sql = "select id,name,age,description from person";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                p = new Person();
                p.setId(rs.getInt(1));
                p.setName(rs.getString(2));
                p.setAge(rs.getInt(3));
                p.setDescription(rs.getString(4));
                persons.add(p);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new SQLException("查询所有数据失败");
        }finally{
            DBUtils.close(rs, ps, conn);
        }
        return persons;
    }
     * */
}