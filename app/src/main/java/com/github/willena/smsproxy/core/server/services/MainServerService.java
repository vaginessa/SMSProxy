package com.github.willena.smsproxy.core.server.services;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.willena.smsproxy.core.common.services.AbstractServiceDataManager;
import com.github.willena.smsproxy.core.common.Messages.Message;

/**
 * Created by Guillaume on 08/07/2017.
 */

public class MainServerService extends AbstractServiceDataManager {


    @Override
    public void onMessageReceived(Message message) {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.initCommunication(getApplicationContext());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
