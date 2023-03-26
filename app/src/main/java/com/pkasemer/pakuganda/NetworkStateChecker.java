package com.pkasemer.pakuganda;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pkasemer.pakuganda.Apis.ApiEndPoints;
import com.pkasemer.pakuganda.Models.UserModel;

public class NetworkStateChecker extends BroadcastReceiver {
    private Context context;
    private ApiEndPoints apiEndPoints;
    UserModel userModel;
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;


    }



}
