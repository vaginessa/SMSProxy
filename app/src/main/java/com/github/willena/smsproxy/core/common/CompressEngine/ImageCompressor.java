package com.github.willena.smsproxy.core.common.CompressEngine;

/**
 * Created by Guillaume on 09/07/2017.
 */

class ImageCompressor extends GenericCompressor {

    private static ImageCompressor instance;

    public static ImageCompressor getInstance() {
        if (instance == null)
            instance = new ImageCompressor();
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
