package game.SpringBoot.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import game.SpringBoot.common.DBUtils;
import game.SpringBoot.model.UserInfo;



public class UserDao{

    public void add(UserInfo userInfo) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "replace into users(openid,token,sessionKey,updateTime)values(?,?,?,?,?)";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, userInfo.openid);
            ps.setString(2, userInfo.token);
            ps.setString(3, userInfo.session_key);
            ps.setTimestamp(4,new Timestamp(userInfo.updateTime));

            ps.executeUpdate();

        }catch(SQLException e){
            throw new SQLException("添加数据失败");
        }finally{
            DBUtils.close(null, ps, conn);
        }
    }

    public UserInfo findByToken(String token) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserInfo userInfo = null;
        String sql = "select token,openId,sessionKey,createTime,updateTime from users where token=?";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if(rs.next()){
            	userInfo = new UserInfo();
            	userInfo.token = rs.getString(1);
            	userInfo.openid = rs.getString(2);
            	userInfo.session_key = rs.getString(3);
            	userInfo.createTime = rs.getTimestamp(4).getTime();
            	userInfo.updateTime = rs.getTimestamp(5).getTime();
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new SQLException("根据sessionId查询数据失败");
        }finally{
            DBUtils.close(rs, ps, conn);
        }
        return userInfo;
    }
    
    public UserInfo findByOpenId(String openId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        UserInfo userInfo = null;
        String sql = "select token,openId,sessionKey,createTime,updateTime from users where openId=?";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, openId);
            rs = ps.executeQuery();
            if(rs.next()){
            	userInfo = new UserInfo();
            	userInfo.token = rs.getString(1);
            	userInfo.openid = rs.getString(2);
            	userInfo.session_key = rs.getString(3);
            	userInfo.createTime = rs.getTimestamp(4).getTime();
            	userInfo.updateTime = rs.getTimestamp(5).getTime();
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new SQLException("根据openId查询数据失败");
        }finally{
            DBUtils.close(rs, ps, conn);
        }
        return userInfo;
    }

    public void update(UserInfo p) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "update users set token=?,sessionKey=?,updateTime=?";
        try{
            conn = DBUtils.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, p.token);
            ps.setString(2, p.session_key);
            ps.setTimestamp(3,new Timestamp(System.currentTimeMillis()));
            
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            throw new SQLException("更新数据失败");
        }finally{
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