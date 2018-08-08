package game.SpringBoot.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class LogUtils
{
	private static Logger logger = null;
	
	static
	{
		logger = LoggerFactory.getLogger(LogUtils.class);
	}

	public static final Logger getLogger()
	{
		return logger;
	}
}