package com.github.willena.smsproxy.core.common.CompressEngine;

import com.github.willena.smsproxy.Utils.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by Guillaume on 09/07/2017.
 */

//TODO: Some reworking in the uncompress function cen be needed.

class BinaryCompressor extends GenericCompressor {

    private static BinaryCompressor instance;

    public static BinaryCompressor getInstance() {
        if (instance == null)
            instance = new BinaryCompressor();
        return instance;
    }

    @Override
    protected String compressToString(String data) {
        return StringUtils.removeNonVisibleChars(this.compress(data));
    }

    private byte[] compress(byte[] bytesToCompress) {
        Deflater deflater = new Deflater();
        deflater.setInput(bytesToCompress);
        deflater.finish();

        byte[] bytesCompressed = new byte[Short.MAX_VALUE];

        int numberOfBytesAfterCompression = deflater.deflate(bytesCompressed);

        byte[] returnValues = new byte[numberOfBytesAfterCompression];

        System.arraycopy(bytesCompressed, 0, returnValues, 0, numberOfBytesAfterCompression);

        return returnValues;
    }

    private byte[] compress(String stringToCompress) {
        byte[] returnValues = null;

        try {

            returnValues = this.compress(stringToCompress.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        return returnValues;
    }

    @Override
    protected String unCompressToString(String data) {

        byte[] bytesDecompressed = this.uncompress(StringUtils.restoreNonVisibleChars(data));

        if (bytesDecompressed == null)
            return null;

        String returnValue = null;

        try {
            returnValue = new String(bytesDecompressed, 0, bytesDecompressed.length, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            //uee.printStackTrace();
            return null;
        }

        return returnValue;
    }

    private byte[] uncompress(byte[] bytesToDecompress) {
        byte[] returnValues = null;

        Inflater inflater = new Inflater();
        inflater.setInput(bytesToDecompress, 0, bytesToDecompress.length);

        List<Byte> bytesDecompressedSoFar = new ArrayList<>();

        try {
            while (!inflater.needsInput()) {
                byte[] bytesDecompressedBuffer = new byte[bytesToDecompress.length];

                int numberOfBytesDecompressedThisTime = inflater.inflate(bytesDecompressedBuffer);

                for (int b = 0; b < numberOfBytesDecompressedThisTime; b++) {
                    bytesDecompressedSoFar.add(bytesDecompressedBuffer[b]);
                }
            }

            returnValues = new byte[bytesDecompressedSoFar.size()];
            for (int b = 0; b < returnValues.length; b++) {
                returnValues[b] = bytesDecompressedSoFar.get(b);
            }

        } catch (DataFormatException dfe) {
            //dfe.printStackTrace();
        }

        inflater.end();

        return returnValues;
    }


}
