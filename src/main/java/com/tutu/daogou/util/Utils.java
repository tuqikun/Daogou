package com.tutu.daogou.util;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;


/**
 * Created by James on 16/8/12.
 */
@Slf4j
public class Utils {

  /**
   * @param e
   *            异常类
   * @return 拼接打印 exception 栈内容
   */
  public static String stacktrace(Throwable e) {
    StringBuilder stack_trace = new StringBuilder();
    while (e != null) {
      String error_message = e.getMessage();
      error_message = error_message == null ? "\r\n" : error_message.concat("\r\n");
      stack_trace.append(error_message);
      stack_trace.append("<br>");
      for (StackTraceElement string : e.getStackTrace()) {
        stack_trace.append(string.toString());
        stack_trace.append("<br>");
      }
      e = e.getCause();
    }
    return stack_trace.toString();
  }

  public static String getLocalHostName(){
    try{
      String HostName = InetAddress.getLocalHost().getHostName();
//            HostName.substring(0 , HostName.indexOf("."));
      return HostName;
    }catch(UnknownHostException e){
      log.error("取得hostname失败",e);
      return "";
    }

  }

  public static String getLocalIP(){
    String ip="";
//        try{
//
//            ip = InetAddress.getLocalHost().getHostAddress();
//
//
//            return ip;
//        }catch(UnknownHostException e1){
//            log.error("通过InetAddress.getLocalHost().getHostAddress()取得hostip失败",e1);
//        }

    try{
      Enumeration e = NetworkInterface.getNetworkInterfaces();
      while(e.hasMoreElements())
      {
        NetworkInterface n = (NetworkInterface) e.nextElement();
        Enumeration ee = n.getInetAddresses();
        while (ee.hasMoreElements())
        {
          InetAddress i = (InetAddress) ee.nextElement();

          // aka site localaddress
          if(i.isSiteLocalAddress()){
            return i.getHostAddress();
          }
        }

      }
      return ip;
    }catch(SocketException e2){
      log.error("取得hostip失败",e2);
      return ip;
    }


  }

  public static Date addDate(Date date,int offset){
    Calendar calendar   =  Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
    calendar.setTime(date);
    calendar.add(calendar.DATE,offset);//把日期往后增加.整数往后推,负数往前移动
    date=calendar.getTime();   //
    return date;

  }

  public static double doubleAdd(Double v1, Double v2) {
    if (Double.isNaN(v1)) {
      v1 = 0.0;
    }
    if (Double.isNaN(v2)) {
      v2 = 0.0;
    }
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.add(b2).doubleValue();
  }

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }

}
