package game.SpringBoot.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import game.SpringBoot.common.LogUtils;
import game.SpringBoot.common.MyHttpClient;
import game.SpringBoot.controller.MesseDispatcher.MsgHandler;
import game.SpringBoot.manager.UserManager;
import game.SpringBoot.message.ClientMessages;
import game.SpringBoot.message.ClientMessages.ClientMessageHeader;
import game.SpringBoot.message.ClientMessages.EnterGame;
import game.SpringBoot.message.ClientMessages.RequestMsgData;
import game.SpringBoot.message.ClientMessages.Response;
import game.SpringBoot.message.MessageCode;
import game.SpringBoot.model.UserInfo;

@RestController
public class UserController
{
	//游戏类型
	static class GameType
	{
		public static final int GAME_INVALID        = 0;   //无效类型
		public static final int GAME_DRAWLOT        = 1;   //求签
		public static final int GAME_TRY_TEST       = 2;   //测试
	}
	
	@Autowired
	private LoginHandler loginHandler;
	
	@Autowired
	private QuestionHandler questionHandler;

	@Autowired
	private DrawHandler drawHandler;
	
	@Autowired
	private TestHandler testHandler;

	@Autowired
	private LoginValidate loginValidate;
	
	private MesseDispatcher messeDispatcher = new MesseDispatcher();

	
    @PostConstruct
    public void init()
    {
    	messeDispatcher.registerMsg(ClientMessages.MSG_LOGIN,        loginHandler,    "onLogin");
		messeDispatcher.registerMsg(ClientMessages.MSG_TEST,         testHandler,     "onTest");
		
		messeDispatcher.registerMsg(ClientMessages.MSG_ENTER_DRAW,   drawHandler,     "onEnterDraw");
		messeDispatcher.registerMsg(ClientMessages.MSG_ENTER_TEST,   questionHandler, "onEnterTest");
		
		messeDispatcher.registerMsg(ClientMessages.MSG_DRAW,         drawHandler,     "onDraw");
		messeDispatcher.registerMsg(ClientMessages.MSG_THROW_GRAIL,  drawHandler,     "onThrowGrail");
		messeDispatcher.registerMsg(ClientMessages.MSG_ANSWER_DRAW,  drawHandler,     "onAnswerTheDraw");
		
		messeDispatcher.registerMsg(ClientMessages.MSG_QUIZ_INFO,    questionHandler, "onGetQuizInfo");
		messeDispatcher.registerMsg(ClientMessages.MSG_QUIZ_SUBMIT,  questionHandler, "onSubmitAnswers");
    }

	@GetMapping("/test")
	public String test()
	{
		return onUserAction("{\"msg_id\":100,\"msg_data\":{\"token\":\"0c148139ff6747a98b02c53b5b430f42\"}}");
	}
	
	@PostMapping("/userAction")
    public @ResponseBody String userAction(HttpServletRequest request)
	{
		String postData = MyHttpClient.getPostData(request);
		LogUtils.getLogger().info("postData="+postData);
		
		String ret = onUserAction(postData);
		
		LogUtils.getLogger().info("responseData="+ret);
		
		return ret;
    }
	
	private String onUserAction(String postData)
	{
		ClientMessageHeader message = JSONObject.parseObject(postData, ClientMessageHeader.class);
		
		if(message == null)
		{
			LogUtils.getLogger().info("parse ClientMessageHeader error.postData="+postData);
			return "";
		}
		
		//交给handler处理的数据
		String handlerMsg = message.msgData;
		int messgeId = message.msgId;
		
		UserInfo userInfo = null;
		//非登录信息，验证是否登录
		if(messgeId != ClientMessages.MSG_LOGIN)
		{
			RequestMsgData req = JSONObject.parseObject(message.msgData, RequestMsgData.class);
			if(req == null || req.token == null)
	        {
	        	LogUtils.getLogger().info("parse RequestMsgData error.msgData="+message.msgData);
	        	return "";
	        }

			handlerMsg = req.data;
			
			//验证登录
			userInfo = loginValidate.validate(req.token);
			if(userInfo == null)
			{
				Response rsp = new Response();
			    rsp.resultCode = MessageCode.CODE_RELOGIN;
			    rsp.msg        = "请用code重新登录。";
			    	
			    return JSONObject.toJSONString(rsp);
			}
			//更新缓存过期时间
			userInfo.expireTime = System.currentTimeMillis()+UserManager.UserCacheTime;
		}
		
		//消息id转换
		if(messgeId == ClientMessages.MSG_ENTER_GAME)
		{
			EnterGame enterGame = JSONObject.parseObject(handlerMsg, EnterGame.class);
			switch (enterGame.gameType)
			{
			case GameType.GAME_DRAWLOT:
				messgeId = ClientMessages.MSG_ENTER_DRAW;
				break;
			case GameType.GAME_TRY_TEST:
				messgeId = ClientMessages.MSG_ENTER_TEST;
				break;
			default:
				break;
			}
		}
		
		//消息派发
		MsgHandler handler = messeDispatcher.getHandler(messgeId);
		if(handler != null)
		{
			return handler.invoke(userInfo,handlerMsg);
		}
		else
		{
			Response rsp = new Response();
		    rsp.resultCode = MessageCode.FAILED;
		    rsp.msg        = "no message handler.";
		    	
		    return JSONObject.toJSONString(rsp);
		}
	}
}
