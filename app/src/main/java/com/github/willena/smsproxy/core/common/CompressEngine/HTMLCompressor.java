package com.github.willena.smsproxy.core.common.CompressEngine;

/**
 * Created by Guillaume on 09/07/2017.
 */

class HTMLCompressor extends GenericCompressor {

    private static HTMLCompressor instance;

    public static HTMLCompressor getInstance() {
        if (instance == null)
            instance = new HTMLCompressor();
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
