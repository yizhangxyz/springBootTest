package game.SpringBoot.message;

public class ClientMessages
{
	public static int MSG_LOGIN = 1;     //登录消息id
	public static int MSG_TEST  = 100;   //测试消息id
	
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
	
	
	//返回客户端数据----------------------------------------------------------------------
	//发生错误
	public static class ErrorRsp
	{
		public int errcode;
		public String errmsg;
	}
	
	//登录成功发送用户数据
	public static class UserLoginRsp
	{
		public String token;
	} 
}
