package net.dgistudio.guillaume.webbysms;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Guillaume on 10/01/2016.
 */
public class Sessions{
    private long id;
    private String date;
    private String sessionName;
    private String contactName;
    private String contactNumber;
    private String lastUse;
    private long rxSMS;
    private long txSMS;
    private boolean hideMsg;

    protected Sessions() {

    }


    public void setId(long id)
    {
        this.id = id;
    }
    public void setDate(String tms)
    {
        this.date = tms;
    }
    public void setSessionName(String name){
         this.sessionName = name;
    }
    public void setContactName(String name){
         this.contactName = name;
    }
    public void setContactNumber(String number){
         this.contactNumber = number;
    }
    public void setLastUse(String tms){
         this.lastUse = tms;
    }
    public void setRxSMS(long number){
         this.rxSMS = number;
    }
    public void setTxSMS(long number){
         this.txSMS = number;
    }
    public void setHideMsg(boolean isHide) { this.hideMsg = isHide;}


// Get method !!

    public long getId()
    {
        return this.id;
    }
    public String getDate()
    {
        return this.date;
    }
    public String getSessionName(){
        return this.sessionName;
    }
    public String getContactName(){
        return this.contactName;
    }
    public String getContactNumber(){
        return this.contactNumber;
    }
    public String getLastUse(){
        return this.lastUse;
    }
    public long getRxSMS(){
        return this.rxSMS;
    }
    public long getTxSMS(){
        return this.txSMS;
    }
    public boolean getHideMsg(){
        return this.hideMsg;
    }


}
