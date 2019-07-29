package com.tutu.daogou.util;

import tool.util.StringUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 
 * @author xuegongjian
 * 
 */
public abstract class Detect {

    public static final String ATTRIBUTE_ID = "id";

    public static final short INVALID_NUMBER_VALUE = 0;

    public static final String EMPTY_STRING = "";

    public static final String DELIMITER = ",";

    /** notEmpty */
    public static boolean notEmpty(String string) {
        return null != string && !EMPTY_STRING.equals(string);
    }

    public static boolean notEmpty(Collection<?> collection) {
        // List<Object> list = new ArrayList<Object>();
        // list.add(null);
        // System.out.println(notEmpty(list)); //true

        if (null != collection) {
            Iterator<?> iterator = collection.iterator();
            if (null != iterator) {
                while (iterator.hasNext()) {
                    if (null != iterator.next()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return null != map && !map.isEmpty();
    }

    public static boolean notEmpty(short[] array) {
        return null != array && array.length > 0;
    }

    public static boolean notEmpty(int[] array) {
        return null != array && array.length > 0;
    }

    public static boolean notEmpty(long[] array) {
        return null != array && array.length > 0;
    }

    public static <T> boolean notEmpty(T[] array) {
        return null != array && array.length > 0;
    }

    /** is */
    public static boolean isNegative(double value) {
        return value < 0;
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    public static boolean isPositive(Long value) {
        return (null != value && value > 0);
    }

    public static boolean isPositive(BigDecimal value) {
        if (null == value) {
            return false;
        }
        int r = value.compareTo(BigDecimal.ZERO);
        return !(r <= 0);
    }

    public static boolean isNegative(BigDecimal value) {
        return !isPositive(value);
    }

    public static boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    public static boolean isFalse(Boolean value) {
        return Boolean.FALSE.equals(value);
    }

    /**  */
    public static double max(double a, double b) {
        return (a > b) ? a : b;
    }

    public static double min(double a, double b) {
        return (a < b) ? a : b;
    }



    /** equals */
    public static boolean equals(Class<?> clazz1, Class<?> clazz2) {
        return clazz1 == null ? clazz2 == null : clazz1.equals(clazz2);
    }

    public static boolean equals(Boolean boolean1, Boolean boolean2) {
        return boolean1 == null ? boolean2 == null : boolean1.booleanValue() == boolean2.booleanValue();
    }

    /** contains */
    public static boolean contains(long value, long[] values) {
        if (notEmpty(values)) {
            for (long v : values) {
                if (v == value) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(BigDecimal value, BigDecimal[] values) {
        if (null == value) {
            return false;
        }
        if (notEmpty(values)) {
            for (BigDecimal v : values) {
                if (v.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    // public static boolean contains(String value, String[] values) {
    // for (String v : values) {
    // if (null == value) {
    // if (null == v) {
    // return true;
    // }
    // } else if (value.equals(v)) {
    // return true;
    // }
    //
    // }
    // }
    // return false;
    // }

    public static <E> boolean contains(E one, List<E> list) {
        if (notEmpty(list) && null != one) {
            for (E item : list) {
                if (one.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <E> boolean contains(E one, E[] many) {
        if (notEmpty(many) && null != one) {
            return Arrays.asList(many).contains(one);
        }
        return false;
    }

    /** as */
    public static boolean asBoolean(Object value) {
        return (value instanceof Boolean) ? ((Boolean) value).booleanValue() : Boolean.parseBoolean(asString(value));
    }

    public static Boolean asWrapperBoolean(Object value) {
        return null == value ? null : asBoolean(value);
    }

    public static short asShort(Object value) {
        return (value instanceof Short) ? ((Short) value).shortValue() : Short.parseShort(asString(value));
    }

    public static Short asWrapperShort(Object value) {
        return null == value || !Detect.notEmpty(value.toString()) ? null : asShort(value);
    }

    public static int asInt(Object value) {
        return (value instanceof Number) ? ((Number) value).intValue() : Integer.parseInt(asString(value));
    }

    public static int asInt(Object value, int nullValue) {
        return null == value || !Detect.notEmpty(value.toString()) ? nullValue : asInt(value);
    }

    public static Integer asWrapperInteger(Object value) {
        return null == value ? null : asInt(value);
    }

    public static long asLong(Object value) {
        return (value instanceof Number) ? ((Number) value).longValue() : Long.parseLong(asString(value));
    }

    public static BigDecimal asBigDecimal(Object value) {
        return (value != null && Detect.notEmpty(value.toString())) ? new BigDecimal(value.toString()) : BigDecimal.ZERO;
    }

    public static long asLong(Object value, long nullValue) {
        return null == value || !Detect.notEmpty(value.toString()) ? nullValue : asLong(value);
    }

    public static Long asWrapperLong(Object value) {
        return null == value ? null : asLong(value);
    }

    public static double asDouble(Object value, double nullValue) {
        return null == value || !Detect.notEmpty(value.toString()) ? nullValue : asDouble(value);
    }

    public static double asDouble(Object value) {
        try {
            return (value instanceof Double) ? ((Double) value).doubleValue() : Double.parseDouble(asString(value));
        } catch (Exception e) {
            return 0;
        }
    }

    public static byte asByte(Object value, byte nullValue) {
        return null == value || !Detect.notEmpty(value.toString()) ? nullValue : asByte(value);
    }

    public static byte asByte(Object value) {
        try {
            return (value instanceof Byte) ? ((Byte) value).byteValue() : Byte.parseByte(asString(value));
        } catch (Exception e) {
            return 0;
        }
    }

    public static Double asWrapperDouble(Object value) {
        return null == value ? null : asDouble(value);
    }

    public static String asString(Object object) {
        return (null == object) ? null : StringUtil.trim(String.valueOf(object));
    }

    public static String asString(Object value, String nullValue) {
        return null == value ? nullValue : asString(value);
    }

    @SuppressWarnings("unchecked")
    public static <E> E[] asArray(List<E> list) {
        return notEmpty(list) ? (E[]) list.toArray() : null;
    }

    /** union */
    public static <E> List<E> union(List<E>... lists) {
        List<E> result = new ArrayList<E>();
        if (null != lists) {
            for (List<E> list : lists) {
                if (notEmpty(list)) {
                    result.addAll(list);
                }
            }
        }
        return (List<E>) escapeEmpty(result);
    }

    public static <E> Set<E> union(Set<E>... sets) {
        Set<E> result = new HashSet<E>();
        if (null != sets) {
            for (Set<E> set : sets) {
                if (notEmpty(set)) {
                    result.addAll(set);
                }
            }
        }
        return escapeEmpty(result);
    }

    public static <E> Collection<E> union(Collection<E>... collections) {
        Collection<E> result = new ArrayList<E>();
        if (null != collections) {
            for (Collection<E> collection : collections) {
                if (notEmpty(collection)) {
                    result.addAll(collection);
                }
            }
        }
        return escapeEmpty(result);
    }


    /** escapeEmpty */
    public static <E> List<E> escapeEmpty(List<E> list) {
        return notEmpty(list) ? list : null;
    }

    public static <E> Set<E> escapeEmpty(Set<E> set) {
        return notEmpty(set) ? set : null;
    }

    // TODO: remove null element in collection
    public static <E> Collection<E> escapeEmpty(Collection<E> collection) {
        return notEmpty(collection) ? collection : null;
    }

    public static String escapeVarchar(String value) {
        return (notEmpty(value) && value.length() > 4000) ? value.substring(0, 3990) : value;
    }

    public static <E> E firstOne(List<E> list) {
        return notEmpty(list) ? list.get(0) : null;
    }

    public static boolean onlyOne(List<?> list) {
        return notEmpty(list) && list.size() == 1;
    }

    /** unmodifiable */
    public static <E> List<E> unmodifiable(List<E> list) {
        return notEmpty(list) ? Collections.unmodifiableList(list) : null;
    }

    public static <E> Set<E> unmodifiable(Set<E> set) {
        return notEmpty(set) ? Collections.unmodifiableSet(set) : null;
    }

    public static boolean between(long value, long floor, long ceil) {
        return value >= floor && value <= ceil;
    }

    public static String trim(String string) {
        return StringUtil.trim(string);
    }

    /*
     * 0 指前面补充零 formatLength 字符总长度为 formatLength d 代表为正数。
     */
    public static String frontCompWithZore(long source, int formatLength) {

        String newString = String.format("%0" + formatLength + "d", source);
        return newString;
    }

    public static boolean validateIpAddress(String ipAddress) {
        if (!notEmpty(ipAddress)) {
            return false;
        }
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern p = Pattern.compile(regex);
        return p.matcher(ipAddress).matches();
    }

    /**
     * if o is null return ""
     * 
     * @param o
     * @return
     */

    public static String plainDoubleObjectToString(Object o, int scale) {
        if (null == o) {
            return "";
        }
        if (Detect.asDouble(o, 0) == 0) {
            return o.toString();
        }
        return new BigDecimal(Detect.asDouble(o)).setScale(scale, BigDecimal.ROUND_HALF_UP).toPlainString();

    }

    public static void main(String[] args) throws Exception {

        Double a = 0.3D;
        System.out.println(plainDoubleObjectToString(a, 2));

        // long[] a = null;
        // System.out.println(notEmpty( a));
        // Short value = null;
        // System.out.println(asShort((Short)value));

        // List<long[]> longValueGroup = grouping(new long[] { 1, 2, 3, 4, 5, 6,
        // 7, 8, 9, 10 }, 5);
        // if (null != longValueGroup) {
        // for (int i = 0; i < longValueGroup.size(); i++) {
        // long[] longValue = longValueGroup.get(i);
        //
        // System.out.println(longValue.length + ": " + join(longValue));
        // }
        // }
        //
        // System.out.println(JsonUtil.marshal(escapeDuplication(new long[] { 1,
        // 2, 2, 5, 2, 2, 1, 2, 3, 4, 1, 2, 3 })));

    }
}
