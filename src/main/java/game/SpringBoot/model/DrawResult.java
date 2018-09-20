package game.SpringBoot.model;

import game.SpringBoot.message.ClientMessages.DrawResultRsp;

public class DrawResult
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
	
	public DrawResultRsp buildResultRsp()
	{
		DrawResultRsp rsp = new DrawResultRsp();
		rsp.title      = this.title;
		rsp.type       = this.type;
		rsp.typeValue  = this.typeValue;
		rsp.verse      = this.verse;
		rsp.lotInfo    = this.lotInfo;
		rsp.answer     = this.answer;
		rsp.lucky      = this.lucky;
		rsp.story      = this.story;
		rsp.imgUrl     = this.imgUrl;
		return rsp;
	}
}
