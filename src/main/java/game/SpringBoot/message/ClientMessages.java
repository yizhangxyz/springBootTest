package game.SpringBoot.message;

import java.util.List;

public class ClientMessages
{
	public static final int MSG_LOGIN    = 1;     //登录
	public static final int MSG_QUIZINFO = 2;     //请求试题
	public static final int MSG_TEST     = 100;   //测试
	
	//消息头
	public static class ClientMessageHeader
	{
		public int    msg_id;
		public String msg_data;
	}
	
	//登录请求
	public static class LoginMsgData
	{
		public String code;
	}
	
	//非登录请求
	public static class RequestMsgData
	{
		public String token;     //用户的每个请求都会带上 
	}
	
	//提交答案
	public static class PostAnswers
	{
		public int question_id;
		public List<Integer> answers;
	}
	
	
	//返回客户端数据----------------------------------------------------------------------
	//普通消息
	public static class CommonRsp
	{
		public int result_code;
		public String msg;
	}
	
	//登录成功消息
	public static class UserLoginRsp
	{
		public int result_code;
		public String msg;
		public String token;
	} 
	
	//答题结果
	public static class QuizRsp
	{
		public int result_code;
		public String msg;
		public int score;
	} 
}
