package pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.BaseActivity;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ALARM_ACTION_INTENT = "smc.alarms";
    static int ALARM_CODE=300;
    BaseActivity activity;

    public BaseActivity getActivity() {
        return activity;
    }

    public void setActivity(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        BaseActivity.createNotification(context);
        setUpNextAlarm(context);



    }
    public void setUpNextAlarm(Context context){
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.setAction(ALARM_ACTION_INTENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_CODE, myIntent, 0);

        long firstTime = System.currentTimeMillis()+1000*5;

        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, firstTime, pendingIntent);
    }
}
