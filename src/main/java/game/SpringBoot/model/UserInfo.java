package game.SpringBoot.model;

public class UserInfo
{
	public String token;         //服务器生成的会话id
	public String openid;        //微信中的唯一标识
	public String session_key;   //微信的session key
	public long createTime;      //账号创建时间
	public long updateTime;      //账号更新时间
	public long expireTime;      //缓存到期时间
}
