package net.dgistudio.guillaume.webbysms;

import java.util.ArrayList;

/**
 * Created by Guillaume on 22/01/2016.
 */
public class Content {

    private static final int MAX_COMBINED_SMS = 255;
    private static final int MAX_LENGTH_EACH_SMS = 160;
    private ArrayList<String> message;
    private String rawMessage;

    public void setMessage(String message) {
        this.rawMessage = message;
    }

    public String compressMessage() {


        String compressedTextData = "";
        // String stringToCompress = "This is a test!";
        String stringToCompress = "When in the course of human events, it becomes necessary for one people to dissolve the bands that bind them...";
        stringToCompress = this.rawMessage;

        return new Compressor().prepareAndCompress(stringToCompress);
    }

   /* public boolean hasToSplit() {

        if (this.rawMessage.length()/MAX_LENGTH_EACH_SMS >= MAX_COMBINED_SMS )
        {
            return true;
        }
        return false;
    }

    public ArrayList<String> splitMessage()
    {
        if (this.hasToSplit())
        {
            for (int i=0; i<this.rawMessage.length(); i+=MAX_COMBINED_SMS*MAX_LENGTH_EACH_SMS)
            {
                message.add(rawMessage.substring(i, Math.min(this.rawMessage.length(), i + (MAX_LENGTH_EACH_SMS*MAX_COMBINED_SMS))));
            }
        }
        return this.message;
    }*/

    public String getRawMessage() {
        return rawMessage;
    }
}
