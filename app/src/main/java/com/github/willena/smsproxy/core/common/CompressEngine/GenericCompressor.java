package com.github.willena.smsproxy.core.common.CompressEngine;

/**
 * Created by Guillaume on 09/07/2017.
 */

public abstract class GenericCompressor {
    protected abstract String compressToString(String data);
    protected abstract String unCompressToString(String data);

    public static String compressData(String data, String mimetype){
        String precompress;
        switch (mimetype){
            case "image":
                precompress = ImageCompressor.getInstance().compressToString(data);
                break;
            case "html":
                precompress = HTMLCompressor.getInstance().compressToString(data);
                break;
            case "css":
                precompress = CSSCompressor.getInstance().compressToString(data);
                break;
            case "js":
                precompress = JSCompressor.getInstance().compressToString(data);
                break;
            case "text":
            default:
                precompress = data;
        }

        return BinaryCompressor.getInstance().compressToString(precompress);
    }

    public static String unCompressData(String data){
        return BinaryCompressor.getInstance().unCompressToString(data);
    }



}
