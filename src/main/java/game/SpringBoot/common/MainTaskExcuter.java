package game.SpringBoot.common;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;

import game.SpringBoot.tools.LogUtils;

public final class MainTaskExcuter
{
	private static class MainTaskExcuterHolder
	{
		public static MainTaskExcuter instance = new MainTaskExcuter();
	}

	public static MainTaskExcuter getInstance()
	{
		return MainTaskExcuterHolder.instance;
	}

	private final ClassCache cache = new ClassCache();
	
	private static final class MessageObject
	{
		public Object obj;
		public Method func;
		public Object[] params;
		public Exception callStack;
	}

	final LinkedBlockingQueue<MessageObject> msgList = new LinkedBlockingQueue<>();
	private Thread worker = null;
	private Timer aliveTimer = null;
	
	private MainTaskExcuter()
	{
		worker = new Thread(new Runnable()
		{
			@Override public void run()
			{
				while (!worker.isInterrupted())
				{
					MessageObject task = null;

					try
					{
						task = msgList.take();
					}
					catch (InterruptedException e)
					{
						LogUtils.getLogger().debug("主线程执行停止");
						break;
					}
					catch (Exception e)
					{
						e.printStackTrace();
						break;
					}

					try
					{
						Method method = task.func;

						if (method != null)
						{
							method.invoke(task.obj, task.params);
						}
					}
					catch (Exception e)
					{
						e.addSuppressed(task.callStack);
						LogUtils.getLogger().error(e.getMessage());
					}

				}
			}
		}, "任务主线程");
		worker.setPriority(7); // 优先级提高
		worker.start();
		
		aliveTimer = new Timer("主线程反射缓存清理");
		aliveTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override public void run()
			{
				MainTaskExcuterHolder.instance.cache.check();
			}
		}, 30 * 60000, 30 * 60000);
	}

	public static void shutdown()
	{
		if (getInstance() != null && getInstance().worker != null)
		{
			getInstance().worker.interrupt();

			try
			{
				getInstance().worker.join(2000);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
		}
	}


	public void invoke(Object obj, String func, Object... params) throws Exception
	{
		MessageObject msg = new MessageObject();
		msg.obj = obj;
		msg.func = getMethod(obj, func, params);
		msg.params = params;
		msg.callStack = new Exception();

		if (msg.func == null)
		{
			throw new Exception();
		}

		msgList.put(msg);
	}

	public void invokeStatic(Class<?> clazz, String func, Object... params) throws Exception
	{
		MessageObject msg = new MessageObject();
		msg.obj = null;
		msg.func = getMethod(clazz, func, params);
		msg.params = params;
		msg.callStack = new Exception();

		if (msg.func == null)
		{
			throw new Exception();
		}

		msgList.put(msg);
	}

	public <T> void replace(T oldOne, T newOne, boolean createNew) throws Exception
	{
		if (newOne == null || oldOne == null)
		{
			return;
		}

		List<MessageObject> appendList = new LinkedList<>();

		for (MessageObject obj : msgList)
		{
			if (obj.obj == oldOne)
			{
				if (createNew)
				{
					MessageObject newObj = new MessageObject();
					newObj.obj = newObj;
					newObj.func = getMethod(newObj, obj.func.getName(), obj.params);
					newObj.params = obj.params;

					appendList.add(newObj);
				}
				else
				{
					obj.obj = newOne;
					obj.func = getMethod(obj.obj, obj.func.getName(), obj.params);
				}
			}
		}

		msgList.addAll(appendList);
	}

	private Method getMethod(Object obj, String func, Object[] params) throws Exception
	{
		Class<?> clazz = obj.getClass();
		return getMethod(clazz, func, params);
	}

	private Method getMethod(Class<?> clazz, String func, Object[] params) throws Exception
	{
		return cache.get(clazz, func, params);
	}
}
