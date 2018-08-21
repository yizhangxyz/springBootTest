package game.SpringBoot.message;

import java.util.List;

public class ClientMessages
{
	public static final int MSG_LOGIN           = 1;     //登录
	public static final int MSG_QUIZINFO        = 2;     //请求试题
	public static final int MSG_TEST            = 100;   //测试
	
	public static final int MSG_DRAW            = 101;   //求签
	public static final int MSG_THROW_GRAIL     = 102;   //投掷圣杯
	public static final int MSG_ANSWER_DRAW     = 103;   //解签
	
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
		public int quiz_id;
		public List<String> answers;
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
		public String result_msg;
		public int score;
	} 
	
	//求签应答
	public static class DrawRsp
	{
		public int resultIndex;
		public String resultMsg;
	} 
	
	//投掷杯子应答
	public static class ThrowCupRsp
	{
		public boolean isValid;
		public int validTimes;
		public int times;
	} 
	
	//解签应答
	public static class DrawResultRsp
	{
		public String title;     //标题
		public String type;      //中签类型
		public String verse;     //诗曰
		public String lotInfo;   //签语
		public String answer;    //解签
		public String lucky;     //仙机
		public String story;     //故事
		public String imgUrl;    //图解
	}
}
