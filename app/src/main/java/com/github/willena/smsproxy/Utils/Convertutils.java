package com.github.willena.smsproxy.Utils;

/**
 * Created by guill on 12/08/2017.
 */

public class Convertutils {
    public static int byteToUnsignedInt(byte b) {
        return 0x00 << 24 | b & 0xff;
    }


}
