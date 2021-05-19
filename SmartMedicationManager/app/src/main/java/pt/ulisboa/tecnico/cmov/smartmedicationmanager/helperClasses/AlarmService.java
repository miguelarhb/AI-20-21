package pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.AlarmActivity;

public class AlarmService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        YourTask();

        return Service.START_STICKY;
    }

    private void YourTask() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "smc:alarm");
        wl.acquire(10*60*1000L /*10 minutes*/);
        AlarmReceiver alarmReceiver = new AlarmReceiver();

        alarmReceiver.setUpNextAlarm(getApplicationContext());

        Log.d("fds", "yo");

        Intent subIntent = new Intent(getApplicationContext(), AlarmActivity.class);
        subIntent.setAction(Intent.ACTION_MAIN);
        subIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(subIntent);

        wl.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
