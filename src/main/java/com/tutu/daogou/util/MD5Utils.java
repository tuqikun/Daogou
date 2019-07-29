package com.tutu.daogou.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ziqing.chen
 * on 2017/6/14.
 */
public class MD5Utils {

    // ȫ������
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public MD5Utils() {
    }

    // ������ʽΪ���ָ��ַ���
    private static String byteToArrayString( byte bByte ) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if ( iRet < 0 ) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // ������ʽֻΪ����
    @SuppressWarnings( "unused" )
    private static String byteToNum( byte bByte ) {
        int iRet = bByte;
        System.out.println( "iRet1=" + iRet );
        if ( iRet < 0 ) {
            iRet += 256;
        }
        return String.valueOf( iRet );
    }

    // ת���ֽ�����Ϊ16�����ִ�
    private static String byteToString( byte[] bByte ) {
        StringBuilder sBuffer = new StringBuilder();
        for ( int i = 0; i < bByte.length; i++ ) {
            sBuffer.append( byteToArrayString( bByte[i] ) );
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code( String strObj ) {
        if ( strObj == null || "".equals( strObj ) ) {
            return null;
        }
        String resultString = null;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance( "MD5" );
            // md.digest() �ú�������ֵΪ��Ź�ϣֵ�����byte����
            resultString = byteToString( md.digest( strObj.getBytes() ) );
        } catch ( NoSuchAlgorithmException ex ) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static void main( String[] args ) {
        String str = MD5Utils.GetMD5Code( "1234567890123456" );
        System.out.println( str + ",,,," + str.length() );
    }

}
