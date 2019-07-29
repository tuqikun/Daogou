package com.tutu.daogou.util;

import com.alibaba.fastjson.JSONObject;
import org.jboss.logging.Logger;
import tool.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.*;

/**
 * 工具类-反射帮助类
 * 
 * @author yinLiang
 * @version 1.0
 * @date 2015年11月17日 下午5:39:54 Copyright 杭州融都科技股份有限公司  All Rights
 *       Reserved 官方网站：www.erongdu.com 
 *       未经授权不得进行修改、复制、出售及商业使用
 */
public class ReflectUtil {

	private static final Logger logger = Logger.getLogger(ReflectUtil.class);

	public static List<?> PRIMITIVE_TYPES = Arrays.asList(new Class[] {
			char.class, short.class, byte.class, int.class, long.class,
			float.class, double.class, boolean.class, Short.class, Byte.class,
			Integer.class, Long.class, Float.class, Double.class,
			Boolean.class, String.class, Date.class });

	public static boolean isPrimitive(Class<?> type) {
		return PRIMITIVE_TYPES.contains(type);
	}

	public static Object invokeGetMethod(Class<?> claszz, Object o, String name) {
		Object ret = null;
		try {
			Method method = claszz.getMethod("get"
					+ StringUtil.firstCharUpperCase(name));
			ret = method.invoke(o);
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("claszz:" + claszz + ",name:" + name,e);
		}
		return ret;
	}

	public static Object invokeSetMethod(Class<?> claszz, Object o,
			String name, Class<?>[] argTypes, Object[] args) {
		Object ret = null;
		// 非 常量 进行反射
		try {
			if (!checkModifiers(claszz, name)) {
				Method method = claszz.getMethod(
						"set" + StringUtil.firstCharUpperCase(name), argTypes);
				ret = method.invoke(o, args);
			}
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("claszz:" + claszz + ",name:" + name + ",argType:"
					+ argTypes + ",args:" + args,e);
		}
		return ret;
	}

	public static Object invokeSetMethod(Class<?> claszz, Object o,
			String name, Class<?> argType, Object args) {
		Object ret = null;
		// 非 常量 进行反射
		try {
			if (!checkModifiers(claszz, name)) {
				Method method = claszz.getMethod(
						"set" + StringUtil.firstCharUpperCase(name),
						new Class[] { argType });
				ret = method.invoke(o, new Object[] { args });
			}
		} catch (NoSuchMethodException | SecurityException
				| IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			logger.error("claszz:" + claszz + ",name:" + name + ",argType:"
					+ argType + ",args:" + args);
		}
		return ret;
	}

	/**
	 * 校验参数类型 目前只校验是否为 常量
	 * 
	 * @param claszz
	 * @param name
	 * @return 常量返回true，非常量返回false
	 */
	private static boolean checkModifiers(Class<?> claszz, String name) {
		try {
			Field field = claszz.getField(name);
			if (isConstant(field.getModifiers())) {
				return true;
			}
		} catch (NoSuchFieldException e) {
			logger.error(e);
			return false;
		}
		return false;
	}

	/**
	 * 是否为常量
	 * 
	 * @param modifiers
	 * @return 常量返回true，非常量返回false
	 */
	private static boolean isConstant(int modifiers) {
		// static 和 final修饰
		if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
			return true;
		}
		return false;
	}



	public static Method getDeclaredMethod(Object object, String methodName,
			Class<?>... parameterTypes) {
		Method method = null;
		Class<?> clazz = object.getClass();
		while (clazz != Object.class) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
			} catch (Exception e) {
				// 这里异常不能抛出去。
				// 如果这里的异常打印或者往外抛，就不会执行clazz = clazz.getSuperclass(),
				// 最后就不会进入到父类中了
			}
			if (method != null)
				break;
			clazz = clazz.getSuperclass();
		}
		return method;
	}

	public static Map<String, Field> getClassField(Class<?> clazz) {
		Field[] declaredFields = clazz.getDeclaredFields();
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		Map<String, Field> superFieldMap = new HashMap<String, Field>();
		for (Field field : declaredFields) {
			fieldMap.put(field.getName(), field);
		}
		if (clazz.getSuperclass() != null) {
			superFieldMap = getClassField(clazz.getSuperclass());
		}
		fieldMap.putAll(superFieldMap);
		return fieldMap;
	}



	/**
	 * 根据paramsName数组定义内容，反射出Object对应Field的值，然后拼接成字符串返回 拼接规则value1+value2+value3
	 * 例：value1=a,value2=b,value3=c,那么方法返回结果为：abc
	 * 
	 * @param object
	 * @param paramNames
	 * @return
	 */
	public static String fieldValueToString(Object object, String[] paramNames) {
		if (object == null || paramNames == null || paramNames.length <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		StringBuilder detail = new StringBuilder();
		for (String name : paramNames) {
			Object o = ReflectUtil.invokeGetMethod(object.getClass(), object,
					name);
			String value = StringUtil.isNull(o);
			sb.append(value);
			detail.append(name + "=" + value + "&");
		}
		logger.debug("参数拼接明细：" + detail.toString());
		logger.debug("参数拼接结果：" + sb.toString());
		return sb.toString();
	}

	public static String fieldValueToString(Map<String, String> data,
			String[] paramNames) {
		if (data.isEmpty() || paramNames == null || paramNames.length <= 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		StringBuilder detail = new StringBuilder();
		for (String name : paramNames) {
			String value = data.get(name);
			value = StringUtil.isNull(value);
			sb.append(value);
			detail.append(name + "=" + value + "&");
		}
		logger.debug("参数拼接明细：" + detail.toString());
		logger.debug("参数拼接结果：" + sb.toString());
		return sb.toString();
	}

	/**
	 * object 属性名称及属性值组装为 Map，再用Map转Json字符串。 组装规则： 只组装String类型，且不为常量的字段，
	 * 组装时若属性值为空或为null，则不加入Json
	 * 
	 * @param object
	 * @return
	 */
	public static String fieldValueToJson(Object object, String[] paramNames) {
		Map<String, String> map = fieldValueToMap(object, paramNames);
		String str = JSONObject.toJSONString(map);
		return str;
	}

	/**
	 * object 属性名称及属性值组装为 Map，再用Map转Json字符串。 组装规则： 只组装String类型，且不为常量的字段，
	 * 组装时若属性值为空或为null，则不加入Map
	 * 
	 * @param object
	 * @return
	 */
	public static Map<String, String> fieldValueToMap(Object object,
			String[] paramNames) {
		return fieldValueToMap(object, paramNames, true);
	}

	/**
	 * object 属性名称及属性值组装为 Map，再用Map转Json字符串。 组装规则： 只组装String类型，且不为常量的字段，
	 * 组装时若isTrim为true 且 属性值为空或为null，则不加入Map
	 * 
	 * @param object
	 * @param paramNames
	 * @param isTrim
	 * @return
	 */
	public static Map<String, String> fieldValueToMap(Object object,
			String[] paramNames, boolean is_trim) {
		Map<String, String> map = new HashMap<String, String>();
		for (String name : paramNames) {
			Object o = ReflectUtil.invokeGetMethod(object.getClass(), object,
					name);
			String value = StringUtil.isNull(o);
			// 是否去空
			if (is_trim && "".equals(value)) {
				continue;
			}
			map.put(name, value);
		}
		logger.debug("数组反射结果：" + map.toString());
		return map;
	}

	/**
	 * 获取对象的所有属性名
	 *
	 * @param obj
	 * @return
	 */
	public static List<String> getAllFields(Object obj) {
		Class<?> clazz = obj.getClass();
		List<String> fieldNames = new ArrayList<String>();
		Field[] fieldArr = clazz.getDeclaredFields();
		for (int i = 0; i < fieldArr.length; i++) {
			fieldNames.add(fieldArr[i].getName());
		}
		return fieldNames;
	}

	/**
	 * 利用反射获取指定对象的指定属性
	 *
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @return 目标属性的值
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 利用反射获取指定对象里面的指定属性
	 *
	 * @param obj
	 *            目标对象
	 * @param fieldName
	 *            目标属性
	 * @return 目标字段
	 */
	private static Field getField(Object obj, String fieldName) {
		Field field = null;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (NoSuchFieldException e) {
				// 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
			}
		}
		return field;
	}
}
