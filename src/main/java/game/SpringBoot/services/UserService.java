package game.SpringBoot.services;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import game.SpringBoot.dao.UserDao;
import game.SpringBoot.model.UserInfo;

@Component
public class UserService
{
	@Autowired
	private UserDao userDao;
	 
	public UserService()
	{
		// TODO Auto-generated constructor stub
	}
	public void createOrUpdateUser(UserInfo userInfo) throws SQLException
	{
		userDao.createOrUpdateUser(userInfo);
	}
	
	public UserInfo findByToken(String token) throws SQLException
	{
		return userDao.findByToken(token);
	}
}
