package com.github.willena.smsproxy.core.common.CompressEngine;

/**
 * Created by Guillaume on 09/07/2017.
 */

class CSSCompressor extends GenericCompressor {

    private static CSSCompressor instance;

    public static CSSCompressor getInstance() {
        if (instance == null)
            instance = new CSSCompressor();
        return instance;
    }


    @Override
    public String compressToString(String data) {
        return null;
    }

    @Override
    protected String unCompressToString(String data) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
