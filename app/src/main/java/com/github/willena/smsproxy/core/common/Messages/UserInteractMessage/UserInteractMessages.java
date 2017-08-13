package com.github.willena.smsproxy.core.common.Messages.UserInteractMessage;

import com.github.willena.smsproxy.core.common.Messages.Message;

/**
 * Created by Guillaume on 09/07/2017.
 */

public class UserInteractMessages {

    public static Message getUserQueryMessage(){
        return new SimpleMessage("SMSProxy : Request");
    }

    public static Message getUserConfirmationMessage(){
        return new SimpleMessage("SMSProxy : Confirmation");
    }

    public static Message getUserStopQueryMessage(){
        return new SimpleMessage("SMSProxy : Stop");
    }



    //TODO : List to be continued...



}
