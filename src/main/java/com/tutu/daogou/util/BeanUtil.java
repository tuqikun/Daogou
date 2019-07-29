package com.tutu.daogou.util;

import org.jboss.logging.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class BeanUtil {
    private static Logger logger = Logger.getLogger(BeanUtil.class);

    public static Map<String, Object> beanToMap2(Object obj) {
        Map<String, Object> map = new HashMap<>();
        List<String> fieldNames = ReflectUtil.getAllFields(obj);
        for (String fieldName : fieldNames) {
            Object value = ReflectUtil.getFieldValue(obj, fieldName);
            if (value != null) {
                map.put(fieldName, value);
            }
        }
        return map;
    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> map = null;
        if (obj == null) {
            return map;
        }
        try {
            map = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);

                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            logger.error("transBean2Map Error ", e);
        }

        return map;

    }

    /**
     * copy map to bean
     * 
     * @param clazz
     * @param paramMap
     * @return
     */
    public static <T> T mapToBean(Class<T> clazz, Map<String, Object> paramMap) {
        try {
            T t = clazz.newInstance();
            BeanWrapper bw = buildBeanWrapper(t);
            Set<Map.Entry<String, Object>> set = paramMap.entrySet();
            for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
                copyProperty(entry.getKey(), entry.getValue(), bw);
            }
            return t;
        } catch (Exception e) {
            logger.error(e);
            return null;
        }

    }

    private static void copyProperty(String key, Object value, BeanWrapper bw) throws Exception {
        String tempKey = null;
        if (Pattern.compile("\\[\\d+\\]").matcher(key).find()) {
            tempKey = key.substring(0, key.indexOf("["));
        } else {
            tempKey = key;
        }
        if (Pattern.compile("\\.").matcher(tempKey).find()) {
            tempKey = tempKey.substring(0, tempKey.indexOf("."));
        }
        if (tempKey != null) {
            Class propClass = bw.getPropertyType(tempKey);
            if (propClass != null) {
                Object propObj = bw.getPropertyValue(tempKey);
                if (propClass.isPrimitive()) {
                    if (value == null) {
                        bw.setPropertyValue(tempKey, "0");
                    } else {
                        bw.setPropertyValue(tempKey, value);
                    }
                } else if (Collection.class.isAssignableFrom(propClass)) {

                    if (propObj == null) {
                        propObj = ArrayList.class.newInstance();
                        bw.setPropertyValue(tempKey, propObj);
                    }

                    int index = Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));

                    if (Pattern.compile("\\.").matcher(key).find()) {

                        Class elemClass = bw.getPropertyTypeDescriptor(tempKey).getElementTypeDescriptor().getType();

                        Object elemObj = index < ((ArrayList) propObj).size() ? ((ArrayList) propObj).get(index) : null;

                        if (elemObj == null) {
                            elemObj = elemClass.newInstance();
                            ((ArrayList) propObj).add(index, elemObj);
                        }

                        BeanWrapper elemBw = buildBeanWrapper(elemObj);
                        String newKey = key.substring(key.indexOf(".") + 1);
                        copyProperty(newKey, value, elemBw);

                    } else {
                        ((ArrayList) propObj).add(index, value);

                    }
                } else {
                    if (Pattern.compile("\\.").matcher(key).find()) {
                        if (propObj == null) {
                            propObj = propClass.newInstance();
                            bw.setPropertyValue(tempKey, propObj);
                        }
                        BeanWrapper propBw = buildBeanWrapper(propObj);
                        String newKey = key.substring(key.indexOf(".") + 1);
                        copyProperty(newKey, value, propBw);

                    } else {
                        bw.setPropertyValue(tempKey, value);
                    }
                }
            }
        }
    }

    private static BeanWrapper buildBeanWrapper(Object object) throws Exception {
        BeanWrapper bw = new BeanWrapperImpl(object);
        // bw.registerCustomEditor(Date.class, new CustomDateEditor(new
        // SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        // bw.registerCustomEditor(Date.class, new CustomDateEditor(new
        // SimpleDateFormat("yyyy-MM-dd"), true));
        // bw.registerCustomEditor(int.class, new IntEditor());
        return bw;
    }

    // public static boolean isNullOrEmpty(Object obj) {
    // if (obj == null)
    // return true;
    //
    // if (obj instanceof CharSequence)
    // return ((CharSequence) obj).length() == 0;
    //
    // if (obj instanceof Collection)
    // return ((Collection) obj).isEmpty();
    //
    // if (obj instanceof Map)
    // return ((Map) obj).isEmpty();
    //
    // if (obj instanceof Object[]) {
    // Object[] object = (Object[]) obj;
    // if (object.length == 0) {
    // return true;
    // }
    // boolean empty = true;
    // for (int i = 0; i < object.length; i++) {
    // if (!isNullOrEmpty(object[i])) {
    // empty = false;
    // break;
    // }
    // }
    // return empty;
    // }
    // return false;
    // }

    public static void main(String[] args) throws Exception {
        // long t = System.currentTimeMillis();
        // Map map = new HashMap();
        // map.put("name", "测试");
        // map.put("time", "2014-01-01");
        // map.put("list[0].desc", "测试复杂集合");
        // map.put("list[1].desc", "测试复杂集合2");
        // map.put("tlist[0]", "测试简单集合");
        // map.put("tlist[1]", "测试简单集合2");
        // map.put("pageNo", "3");
        // DemoModel dm = mapToBean(DemoModel.class, map);
        // Page page = mapToBean(Page.class, map);
        // System.out.println(dm);

        // System.out.println(bw.getPropertyValue("list"));
        // copyProperty("td.dm.list[0].desc", "123", bw);
        // copyProperty("td.dm.list[1].desc", "456", bw);
        // bw.setPropertyValue("tlist", "123,456");
        // System.out.println(dm.getList().size());
        // System.out.println(dm.getTd().getDm().getName()+"##"+dm.getTd().getDm().getTime());
        // System.out.println(System.currentTimeMillis()-t);

        // System.out.println(int.class.isPrimitive());
        // DemoModel dm2 = new DemoModel();
        // BeanWrapper bw = new BeanWrapperImpl(dm2);
        // System.out.println(bw.getPropertyTypeDescriptor("list"));

        // bw.registerCustomEditor(Date.class, new CustomDateEditor(
        // new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
        // bw.registerCustomEditor(Date.class, new CustomDateEditor(
        // new SimpleDateFormat("yyyy-MM-dd"), true));
        // bw.registerCustomEditor(int.class, new IntEditor());
        //
        // String key = "list[0].desc";
        // System.out
        // .println(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
        // String key2 = "td.desc";
        // String key3 = "name";
        // String key4 = "list[100].desc";
        //
        // BeanWrapper bw1 = buildBeanWrapper(String.class);
        // bw1.setPropertyValue("name", "123");
        //
        // System.out.println(Pattern.compile("\\[\\d+\\]").matcher(key4).find());
        //
        // System.out.println(bw.getPropertyDescriptor("td").getDisplayName());
        // System.out.println(Object.class.isAssignableFrom(String.class));
        // System.out.println(Collection.class.isAssignableFrom(bw
        // .getPropertyType("list")));
        // System.out.println(bw.getPropertyTypeDescriptor("list")
        // .getElementTypeDescriptor().getType());
        // bw.setPropertyValue("list", null);
        // bw.setPropertyValue("test", "0");
        // System.out.println(dm.isTest());
        // bw.setPropertyValue("name", null);
        // bw.setPropertyValue("rst", "0,1".split(","));
        // bw.setPropertyValue("time", "");
        // bw.setPropertyValue("id", null);
        // bw.setPropertyValue("td.desc", "测试");
        // bw.setPropertyValue("list[0].desc", "测试");
        // MutablePropertyValues pvs = new MutablePropertyValues();

        // pvs.addPropertyValue("rst", "0");
        // bw.setPropertyValues(pvs);
        // bw.setPropertyValue("time", "2014-01-21");
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put("name", "love");
        // map.put("time", "2014-01-21");
        // try {
        // //BeanUtils.copyProperties(map, dm);
        // BeanCopier.create(Map.class, DemoModel.class, false).copy(map, dm,
        // null);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // System.out.println(dm.getRst().size() + "#" + dm.getRst().get(0) +
        // "#"
        // + dm.getRst().get(1));
        // System.out.println(dm.getTime());
        // System.out.println(dm.getId());

    }

}
