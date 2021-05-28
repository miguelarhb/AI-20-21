package pt.ulisboa.tecnico.cmov.smartmedicationmanager.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Date;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.AlarmActivity;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.R;

import static pt.ulisboa.tecnico.cmov.smartmedicationmanager.data.GlobalData.CHANNEL_ID;

public class AlarmService extends Service {

    private Vibrator vibrator;

    @Override
    public void onCreate() {
        super.onCreate();

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        YourTask(intent);

        return Service.START_STICKY;
    }

    private void YourTask(Intent i) {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("id", i.getStringExtra("id"));
        intent.putExtra("time", i.getLongExtra("time", -1));

        int genId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, genId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Alarm");

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Medicine time!")
                .setContentText("Alarm")
                .setSmallIcon(R.drawable.bell)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(pendingIntent, true)
                .setStyle(bigText)
                .setSound(alarmSound)
                .build();

        notification.flags |= Notification.FLAG_INSISTENT;

        long[] pattern = { 0, 100, 1000 };
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0));

        startForeground(genId, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        vibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
