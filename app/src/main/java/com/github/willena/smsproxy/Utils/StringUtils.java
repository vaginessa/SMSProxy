package com.github.willena.smsproxy.Utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guill on 12/08/2017.
 */

public class StringUtils {

    public static String removeNonVisibleChars(byte[] data) {
        int i;
        int tempData = 0;
        String strC = "";

        for (i = 0; i < data.length; i++) {
            tempData = Convertutils.byteToUnsignedInt(data[i]);
            int tempDataB = Convertutils.byteToUnsignedInt(data[i]);
            tempData = tempData + 32;
            if (tempData >= 127)
                tempData += 34;
            strC += (char) tempData;
        }

        return strC;
    }

    public static String removeNonVisibleChars(String data) {
        try {
            return removeNonVisibleChars(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] restoreNonVisibleChars(String str)
    {
        //TODO: quoi ? c'est quoi l'interet de la deusième boucle ? pourquoi avoir utilisé un arralist en premier lieux ?
        List<Byte> bytes = new ArrayList<>();
        int i;
        for (i = 0; i < str.length(); i++) {
            int c =  str.charAt(i);
            if (c >= 127 + 34) {
                c -= 34;
            }
            c -=32;
            byte b = (byte) c;

            bytes.add(b);
        }

        byte[] arrayNewByte = new byte[bytes.size()];
        for (i=0;i<bytes.size();i++)
        {
            arrayNewByte[i] = bytes.get(i);
        }

        return arrayNewByte;
    }
}
