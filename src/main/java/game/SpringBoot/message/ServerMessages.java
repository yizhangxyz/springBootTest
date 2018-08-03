package game.SpringBoot.message;

public class ServerMessages
{
	//用code获取用户数据失败
	public static class LoginFailed
	{
		public int errcode;
		public String errmsg;
	} 
	
	//用code获取用户数据成功
	public static class LoginSuccess
	{
		public String session_key;   //微信针对这个用户的sessionId
		public String openid;        //每个账号每次登录都是唯一的
	}
}
