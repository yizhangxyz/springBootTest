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
import game.SpringBoot.message.ClientMessages;
import game.SpringBoot.message.ClientMessages.ClientMessageHeader;
import game.SpringBoot.message.ClientMessages.CommonRsp;
import game.SpringBoot.message.MessageCode;
import game.SpringBoot.model.UserInfo;

@RestController
public class UserController
{
	@Autowired
	private LoginHandler loginHandler;
	
	@Autowired
	private QuestionHandler questionHandler;

	@Autowired
	private TestHandler testHandler;

	@Autowired
	private LoginValidate loginValidate;
	
	private MesseDispatcher messeDispatcher = new MesseDispatcher();

	
    @PostConstruct
    public void init()
    {
    	messeDispatcher.registerMsg(ClientMessages.MSG_LOGIN,    loginHandler,    "onLogin");
    	messeDispatcher.registerMsg(ClientMessages.MSG_QUIZINFO, questionHandler, "onGetQuizInfo");
		messeDispatcher.registerMsg(ClientMessages.MSG_TEST,     testHandler,     "onTest");
    }
	
	@GetMapping("/testLogin")
	public String testLogin()
	{
		return onUserAction("{\"msg_id\":1,\"msg_data\":{\"code\":\"033Py3X80lCT5H1QuTX80kf6X80Py3XC\"}}");
	}
	
	@GetMapping("/testGetQuizInfo")
	public String testGetQuizInfo()
	{
		return onUserAction("{\"msg_id\":2,\"msg_data\":{\"token\":\"98d5b6f3d3ec4fb2bbaccbfddf4c53c0\"}}");
	}
	
	@PostMapping("/userAction")
    public @ResponseBody String userAction(HttpServletRequest request)
	{
		String postData = MyHttpClient.getPostData(request);
		LogUtils.getLogger().info("postData="+postData);
		
		return onUserAction(postData);
    }
	
	private String onUserAction(String postData)
	{
		ClientMessageHeader message = JSONObject.parseObject(postData, ClientMessageHeader.class);
		
		if(message == null)
		{
			LogUtils.getLogger().info("parse ClientMessageHeader error.postData="+postData);
			return "";
		}
		
		UserInfo userInfo = null;
		//非登录信息验证是否登录
		if(message.msg_id != ClientMessages.MSG_LOGIN)
		{
			userInfo = loginValidate.validate(message.msg_data);
			if(userInfo == null)
			{
				CommonRsp rsp = new CommonRsp();
			    rsp.result_code = MessageCode.CODE_RELOGIN;
			    rsp.msg  = "please relogin by code.";
			    	
			    return JSONObject.toJSONString(rsp);
			}
		}
		
		MsgHandler handler = messeDispatcher.getHandler(message.msg_id);
		if(handler != null)
		{
			return handler.invoke(userInfo,message.msg_data);
		}
		else
		{
			CommonRsp rsp = new CommonRsp();
		    rsp.result_code = MessageCode.FAILED;
		    rsp.msg  = "no message handler.";
		    	
		    return JSONObject.toJSONString(rsp);
		}
	}
}
