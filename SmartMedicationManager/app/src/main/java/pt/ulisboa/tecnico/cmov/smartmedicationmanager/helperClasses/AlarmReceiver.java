package pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "smc:alarm");
        wl.acquire(10*60*1000L /*10 minutes*/);

        Intent subIntent = new Intent(context, AlarmActivity.class);
        subIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(subIntent);

        wl.release();

    }
}
