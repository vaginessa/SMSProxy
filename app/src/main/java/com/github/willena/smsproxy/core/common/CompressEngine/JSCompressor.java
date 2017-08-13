package com.github.willena.smsproxy.core.common.CompressEngine;

/**
 * Created by Guillaume on 09/07/2017.
 */

class JSCompressor extends GenericCompressor {

    private static JSCompressor instance;

    public static JSCompressor getInstance() {
        if (instance == null)
            instance = new JSCompressor();
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
