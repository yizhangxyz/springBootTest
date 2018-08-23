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
		public int    msgId;
		public String msgData;
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
		public int quizId;
		public List<String> answers;
	}
	
	
	//返回客户端数据----------------------------------------------------------------------
	
	//应答消息头
	public static class Response
	{
		public int resultCode;
		public String msg;
	}
	
	//登录成功消息
	public static class UserLoginRsp extends Response
	{
		public String token;
	} 
	
	//答题结果
	public static class QuizRsp extends Response
	{
		public int score;
	} 
	
	//求签应答
	public static class DrawRsp extends Response
	{
		public int drawIndex;
		public int drawType;    //类型数值（上、中、下...）
		public String drawMsg;
	} 
	
	//投掷杯子应答
	public static class ThrowCupRsp extends Response
	{
		public boolean isValid;
		public int times;
	} 
	
	public static class DrawRetType
	{
		public static final int Invalid = 0;
		public static final int Great   = 1;
		public static final int Good    = 2;
		public static final int Normal  = 3;
		public static final int Low     = 4;
		public static final int Bad     = 5;
	}
	
	//解签应答
	public static class DrawResultRsp  extends Response
	{
		public String title;     //标题
		public String type;      //中签类型
		public int typeValue;    //类型数值（上、中、下...）
		public String verse;     //诗曰
		public String lotInfo;   //签语
		public String answer;    //解签
		public String lucky;     //仙机
		public String story;     //故事
		public String imgUrl;    //图解
	}
}
