package cn.edu.infomanager.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import cn.edu.infomanager.R;

public class NotificationUtil{
	
	public static void addNotification(Context context,int number, String tickerText, String title, String content, int tag,Intent in){
    	Intent intent = new Intent(in);
    	
    	PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    	
    	Notification notification = new Notification(R.mipmap.ic_launcher, tickerText, System.currentTimeMillis());
//		notification.setLatestEventInfo(context, title, content, pendingIntent);
		notification.number = number;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.defaults = Notification.DEFAULT_SOUND;
		NotificationManager manager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(tag, notification);
    }
    
   public static void cancelNotification(Context context, int tag){
	   NotificationManager notificationService = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	   notificationService.cancel(tag);
   }


	public static void addNotification(Context context, String title, String text, Intent intent, int tag){
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)  //为使它向下兼容低版本，采用这个v4里面的类NotificationCompat.Builder是个不错的选择
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(text)
				.setDefaults(Notification.DEFAULT_ALL)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(title));

		/** 2. Action of the notification */
		Intent resultIntent = intent;
		// Because clicking the notification opens a new ("special") activity, there's
		// no need to create an artificial back stack.
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //设置Activity在一个新的空的任务中启动
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);    //LAG_ONE_SHOT：得到的pi只能使用一次，第二次使用该pi时报错
		//FLAG_NO_CREATE： 当pi不存在时，不创建，c
		//FLAG_CANCEL_CURRENT： 每次都创建一个新的pi
		//FLAG_UPDATE_CURRENT： 不存在时就创建，创建好了以后就一直用它，每次使用时都会更新pi的数据(使用较多)

		/** 3. when click the notification */
		mBuilder.setContentIntent(resultPendingIntent);

		/** 4.publish the notification  */
		NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.notify(tag, mBuilder.build());
	}


}
