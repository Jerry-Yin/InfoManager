package cn.edu.infomanager.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.edu.infomanager.module.Memorandum;
import cn.edu.infomanager.service.AlarmNotifyService;
import cn.edu.infomanager.ui.MemorandumActivity;
import cn.edu.infomanager.ui.ShowMemorandumActivity;

public class AlamrReceiver extends BroadcastReceiver {
    private static final String TAG = "AlamrReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {

        final int rcode = intent.getIntExtra("code", -1);
        final int id = intent.getIntExtra("id", -1);
        final int user_id = intent.getIntExtra("user_id", -1);

        final String summary = intent.getStringExtra("summary");
        final String remark = intent.getStringExtra("remark");
//        final int isRemind = intent.getIntExtra("is_remind", 1); //是否提醒

        int position = intent.getIntExtra("id", 0);
        Memorandum memorandum = new MemorandumService(context).selectInfoByID(position);
        final int isRemind; //是否提醒

//        int is_Remind = 1;
        if (memorandum != null){
             isRemind = memorandum.isRemind();

        }else {
            isRemind = 0;
        }
//        final int finalIs_Remind = is_Remind;
        new Thread() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("tag", rcode);
                if (id < 0) {
                    intent.setClass(context, MemorandumActivity.class);
                } else {
                    intent.setClass(context, ShowMemorandumActivity.class);
                }
                intent.putExtra("id", id);
                intent.putExtra("id", user_id);
//				NotificationUtil.addNotification(context, 0, summary, summary, remark, rcode, intent);

                if (isRemind == 1) {
                    NotificationUtil.addNotification(context, summary, remark, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }
//				//启动服务
//				Intent intent1 = new Intent(context, AlarmNotifyService.class);
//				intent1.putExtra("summary", summary);
//				intent1.putExtra("remark", remark);
//				context.startService(intent1);

            }
        }.run();

        Log.d(TAG, rcode + "  闹钟提醒");
        Intent intent1 = new Intent(context, AlamrReceiver.class);
//		PendingIntent pi = PendingIntent.getBroadcast(context, rcode, intent1, Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getBroadcast(context, rcode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pi);
        Log.d(TAG, rcode + "  取消闹钟提醒");
    }

}
