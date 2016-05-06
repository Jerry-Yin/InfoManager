package cn.edu.infomanager.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.edu.infomanager.util.NotificationUtil;

/**
 * Created by JerryYin on 4/24/16.
 */
public class AlarmNotifyService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationUtil.addNotification(getApplicationContext(), intent.getStringExtra("summary"), intent.getStringExtra("remark"), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
