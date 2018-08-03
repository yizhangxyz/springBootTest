package game.SpringBoot.message;

public class ClientMessages
{
	//登录发送code
	public static class LoginCodeReq
	{
		public String code;
	} 
		
	//发生错误
	public static class ErrorRsp
	{
		public int errcode;
		public String errmsg;
	}
	
	//登录成功发送用户数据
	public static class UserLoginRsp
	{
		public String unionid;
		public String nickname;
		public String headimgurl;
		
		public String openid;
		public String accessToken;
		public String refreshToken;
	} 
}
