package com.tutu.daogou.util.base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import tool.util.StringUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 工具类-64位加密解密工具类
 * @author xx
 * @version 1.0
 * @date 2015年12月25日 上午9:20:34
 * Copyright 杭州融都科技股份有限公司  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class Base64Util {
	
	public static final Logger logger = LoggerFactory.getLogger(Base64Util.class);
	
	/**
	 * 字符串Base64位加密
	 * @param str
	 * @return
	 */
	public static String base64Encode(String str) {
		Base64Encoder encoder = new Base64Encoder();
		String result = encoder.encode(str.getBytes());
		return result;
	}
	
	/**
	 * 字节码数组加密
	 * @param b
	 * @return
	 */
	public static String base64Encode(byte[] b) {
		Base64Encoder encoder = new Base64Encoder();
		String result = encoder.encode(b);
		return result;
	}
	
	/**
	 * 字符串Base64位解密
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String base64Decode(String str) {
		try {
			if (StringUtil.isNotBlank(str)) {
				Base64Decoder decoder = new Base64Decoder();
				String result = decoder.decodeStr(str);
				return result;
			} else {
				return null;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * 字符串解密成数组
	 * @param str
	 * @return
	 */
	public static byte[] base64DecodeToArray(String str) {
		try {
			if (StringUtil.isNotBlank(str)) {
				Base64Decoder decoder = new Base64Decoder();
				return decoder.decodeBuffer(str);
			} else {
				return null;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static boolean isImageFromBase64(String base64Str) {
		boolean flag = false;
		try {
			BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(base64Str)));
			if (null == bufImg) {
				return flag;
			}
			flag = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return flag;
	}
}
