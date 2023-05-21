package com.example.administrator.ClothingRentalSystem.admin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"开机完毕~",Toast.LENGTH_LONG).show();
    }
}
