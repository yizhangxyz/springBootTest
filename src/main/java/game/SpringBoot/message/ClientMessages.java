package game.SpringBoot.message;

import java.util.ArrayList;
import java.util.List;

public class ClientMessages
{
	public static final int MSG_LOGIN           = 1;     //登录
	public static final int MSG_TEST            = 100;   //测试
	
	public static final int MSG_ENTER_GAME      = 10;    //进入游戏
	public static final int MSG_ENTER_DRAW      = 11;    //进入抽签
	public static final int MSG_ENTER_TEST      = 12;    //进入测试
	
	public static final int MSG_DRAW            = 101;   //求签
	public static final int MSG_THROW_GRAIL     = 102;   //投掷圣杯
	public static final int MSG_ANSWER_DRAW     = 103;   //解签
	
	public static final int MSG_QUIZ_INFO       = 201;   //请求试题
	public static final int MSG_QUIZ_SUBMIT     = 202;   //提交测试答案
	
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
		public String data;      //用户的请求数据 
	}
	
	//进入游戏
	public static class EnterGame
	{
		public int gameType;      //游戏id
	}
	
	//获取题目
	public static class GetQuizInfo
	{
		public int quizId;
	}
		
	public static class Answer
	{
		public int questionIndex;
		public String answer;
	}
	
	//提交答案
	public static class PostAnswers
	{
		public int quizId;
		public List<Answer> answers;
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
		public int checkType;
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
	
	//今日求签状态
	public static class DrawlotStateRsp extends Response
	{
		public int playerState;
	} 
	
	//答题结果
	public static class QuizRsp extends Response
	{
		public int score;
	} 

	//返回客户端的一个题目
	public static class QuestionView
	{
		public int questionIndex;
		public int score;
		public String content;
		public int answerType;
		public int answerCount;
		public String answers;
		public String nextQuestions;
	}
	
	//返回客户端的一套题
	public static class QuestionsRsp
	{
		public int quizId;
		public String quizTitle;
		public List<QuestionView> questionsView = new ArrayList<>();
	}
}
