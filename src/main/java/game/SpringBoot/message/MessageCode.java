package game.SpringBoot.message;

//消息码定义
public class MessageCode
{
	public static final int SUCCESS      = 0;
	public static final int FAILED       = 1;
	
	public static final int CODE_RELOGIN = 10;    //请用code重新登录
	public static final int DB_ERROR     = 100;   //数据库错误
}
