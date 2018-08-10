package game.SpringBoot.maintask;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class ClassCache
{
	private final ConcurrentHashMap<Class<?>, ClassInfo> cache = new ConcurrentHashMap<>();

	public Method get(Class<?> clazz, String name, Object... params)
	{
		ClassInfo classInfo = cache.get(clazz);

		if (classInfo == null)
		{
			classInfo = new ClassInfo(clazz);

			ClassInfo prevValue = cache.putIfAbsent(clazz, classInfo);

			if (prevValue != null)
			{
				classInfo = prevValue;
			}
		}

		return classInfo.get(name, params);
	}

	public void check()
	{
		Iterator<Entry<Class<?>, ClassInfo>> iterator = cache.entrySet().iterator();

		while (iterator.hasNext())
		{
			Entry<Class<?>, ClassInfo> entry = iterator.next();

			entry.getValue().check();

			if (entry.getValue().methods.isEmpty())
			{
				iterator.remove();
			}
		}
	}
}

final class ClassInfo
{
	private Class<?> classInfo = null;

	final ConcurrentHashMap<String, MedthodInfo> methods = new ConcurrentHashMap<>();

	public ClassInfo(Class<?> clazz)
	{
		classInfo = clazz;
	}

	public Method get(String name, Object[] params)
	{
		MedthodInfo retMethod = methods.get(name);

		if (retMethod != null)
		{
			return retMethod.get();
		}

		Class<?> clazz = classInfo;

		// 进行参数匹配
		do
		{
			Method[] methods = clazz.getDeclaredMethods();

			for (Method method : methods)
			{
				if (method.getName().equals(name))
				{
					// 进行参数匹配
					if (isParamFix(method.getParameterTypes(), params))
					{
						method.setAccessible(true);
						MedthodInfo prevValue = this.methods.putIfAbsent(name, new MedthodInfo(method));
						return prevValue == null ? method : prevValue.get();
					}
				}
			}

			clazz = clazz.getSuperclass();
		}
		while (!(clazz.equals(Object.class) || clazz.isPrimitive()));

		return null;
	}

	private boolean isParamFix(Class<?>[] types, Object[] params)
	{
		if (types == null)
		{
			types = new Class<?>[0];
		}

		if (params == null)
		{
			params = new Object[0];
		}

		if (types.length != params.length)
		{
			return false;
		}

		for (int i = 0; i < params.length; i++)
		{
			Class<?> clazz = types[i];
			Object data = params[i];

			if (data == null)
			{
				if (clazz.isPrimitive())
				{
					return false;
				}
				else
				{
					continue;
				}
			}
			else if (clazz.isPrimitive())
			{
				clazz = getTrueClass(clazz);
			}

			Class<?> dataType = data.getClass();
			boolean hit = clazz.equals(Object.class);

			do
			{
				if (dataType.equals(clazz))
				{
					hit = true;
					break;
				}

				dataType = dataType.getSuperclass();
			}
			while (!(dataType.equals(Object.class) || dataType.isPrimitive()));

			if (!hit)
			{
				return false;
			}
		}

		return true;
	}

	private Class<?> getTrueClass(Class<?> base)
	{
		if (base.equals(Integer.TYPE))
		{
			return Integer.class;
		}
		else if (base.equals(Double.TYPE))
		{
			return Double.class;
		}
		else if (base.equals(Float.TYPE))
		{
			return Float.class;
		}
		else if (base.equals(Long.TYPE))
		{
			return Long.class;
		}
		else if (base.equals(Short.TYPE))
		{
			return Short.class;
		}
		else if (base.equals(Byte.TYPE))
		{
			return Byte.class;
		}
		else if (base.equals(Boolean.TYPE))
		{
			return Boolean.class;
		}
		else if (base.equals(Character.TYPE))
		{
			return Character.class;
		}
		else
		{
			return base;
		}
	}

	public void check()
	{
		long curTick = System.currentTimeMillis();
		Iterator<Entry<String, MedthodInfo>> iterator = methods.entrySet().iterator();

		while (iterator.hasNext())
		{
			Entry<String, MedthodInfo> entry = iterator.next();

			if (curTick - entry.getValue().lastAccess >= 30 * 60000)
			{
				iterator.remove();
			}
		}
	}
}

final class MedthodInfo
{
	long lastAccess = System.currentTimeMillis();
	private Method method = null;

	public MedthodInfo(Method method)
	{
		this.method = method;
	}

	public Method get()
	{
		lastAccess = System.currentTimeMillis();

		return method;
	}
}