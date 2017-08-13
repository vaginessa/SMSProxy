package com.github.willena.smsproxy.core.client.services;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.willena.smsproxy.core.common.services.AbstractServiceDataManager;
import com.github.willena.smsproxy.core.common.Messages.Message;

/**
 * Created by Guillaume on 09/07/2017.
 */

public class MainClientService extends AbstractServiceDataManager {
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
