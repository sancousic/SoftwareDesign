package com.example.rssparser.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class NetworkIntentService extends IntentService {
    public  NetworkIntentService() {
        super("NetworkIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Bundle bundle = workIntent.getExtras();
        if(bundle == null)
            return;

        Messenger messenger = (Messenger) bundle.get("messenger");
        while (true) {
            try {
                ConnectivityManager manager =
                        (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo info = manager.getActiveNetworkInfo();
                boolean isConnected = info != null && info.isConnected();

                Message msg = Message.obtain();
                Bundle data = new Bundle();
                data.putBoolean("isConnected", isConnected);
                msg.setData(data);
                messenger.send(msg);
                Thread.sleep(3000);
            }
            catch (InterruptedException e) {
                System.out.println(e);
            }
            catch (RemoteException e) {
                System.out.println(e);
            }
        }
    }
}
