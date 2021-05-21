package pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.services.AlarmService;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.services.RescheduleAlarmService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("fds", "Alarm Reboot");
            startRescheduleAlarmsService(context);
        }
        else {
            Log.d("fds", "Alarm Received");
            Log.d("fds", intent.getStringExtra("id"));
            startAlarmService(context, intent);
        }
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent intentService = new Intent(context, AlarmService.class);
        intentService.putExtra("id", intent.getStringExtra("id"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }

    private void startRescheduleAlarmsService(Context context) {
        Intent intentService = new Intent(context, RescheduleAlarmService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService);
        } else {
            context.startService(intentService);
        }
    }
}
