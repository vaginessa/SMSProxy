package net.dgistudio.guillaume.webbysms;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by Guillaume on 22/01/2016.
 */

public class Compressor {


    public Compressor() {
    }

    public static int byteToUnsignedInt(byte b) {
        return 0x00 << 24 | b & 0xff;
    }

    public byte[] compress(byte[] bytesToCompress) {
        Deflater deflater = new Deflater();
        deflater.setInput(bytesToCompress);
        deflater.finish();

        byte[] bytesCompressed = new byte[Short.MAX_VALUE];

        int numberOfBytesAfterCompression = deflater.deflate(bytesCompressed);

        byte[] returnValues = new byte[numberOfBytesAfterCompression];

        System.arraycopy(bytesCompressed, 0, returnValues, 0, numberOfBytesAfterCompression);

        return returnValues;
    }

    public byte[] compress(String stringToCompress) {
        byte[] returnValues = null;

        try {

            returnValues = this.compress(stringToCompress.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        return returnValues;
    }

    public byte[] decompress(byte[] bytesToDecompress) {
        byte[] returnValues = null;

        Inflater inflater = new Inflater();

        int numberOfBytesToDecompress = bytesToDecompress.length;

        inflater.setInput(bytesToDecompress, 0, numberOfBytesToDecompress);

        int numberOfBytesDecompressedSoFar = 0;
        List<Byte> bytesDecompressedSoFar = new ArrayList<>();

        try {
            while (!inflater.needsInput()) {
                byte[] bytesDecompressedBuffer = new byte[numberOfBytesToDecompress];

                int numberOfBytesDecompressedThisTime = inflater.inflate(bytesDecompressedBuffer);

                numberOfBytesDecompressedSoFar += numberOfBytesDecompressedThisTime;

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
            Log.i("COMPRESSOR", "decompress: --------------------------------");
            Log.i("COMPRESSOR", "decompress: --------------------------------");
            Log.i("COMPRESSOR", "decompress: " + dfe.getMessage());
            Log.i("COMPRESSOR", "decompress: --------------------------------");
            Log.i("COMPRESSOR", "decompress: --------------------------------");
        }

        inflater.end();

        return returnValues;
    }

    public String decompressToString(byte[] bytesToDecompress) {

        byte[] bytesDecompressed = this.decompress(bytesToDecompress);

        if (bytesDecompressed == null)
            return null;

        String returnValue = null;

        try {
            returnValue = new String(bytesDecompressed, 0, bytesDecompressed.length, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            //uee.printStackTrace();
            Log.d("COMPRESSOR", "decompressToString: ERROR : " + uee.getMessage());
            return null;
        }

        return returnValue;
    }

    public String prepareAndDecompress(String str)
    {
        Log.d("COMPRESSOR", "prepareAndDecompress: str : " + str);
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
           // Log.d("DATA", "number ----> " + c + " b " + b + " bB "+  Content.byteToUnsignedInt(b) +" char  ---> " + (char) c);
        }

        Log.d("COMPRESSOR", "prepareAndDecompress: SIZE : " + bytes.size());
        byte[] arrayNewByte = new byte[bytes.size()];
        for (i=0;i<bytes.size();i++)
        {
            arrayNewByte[i] = bytes.get(i);
        }

        Log.d("String", "DataT: " + bytes.size() + " " + arrayNewByte.length + " " + str.length());
        String stringDecompressed = this.decompressToString(arrayNewByte);
        if (stringDecompressed == null)
            return null;
        Log.d("DATA decoded", "content :" + stringDecompressed+ " len: "+ stringDecompressed.length());
        return stringDecompressed;
    }

    public String prepareAndCompress(String str) {
        byte[] bytesCompressed = this.compress(str);

        int i;
        int tempData = 0;
        String strC = "";

        for (i = 0; i < bytesCompressed.length; i++) {
            tempData = byteToUnsignedInt(bytesCompressed[i]);
            int tempDataB = byteToUnsignedInt(bytesCompressed[i]);
            tempData = tempData + 32;
            if (tempData >= 127)
                tempData += 34;
            strC += (char) tempData;
            // Log.d("DATA ", "DATA AF-----> "+tempData+"  DATA BE ----> "+ tempDataB + " Char ---> "+ (char)tempData);
        }
        Log.d("Compress", "byte " + bytesCompressed.length + " Ori " + str.length());
        Log.d("Compressbs", "new " + strC.length() + " content: " + strC);

        return strC;
    }

}

