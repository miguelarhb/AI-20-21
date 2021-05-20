package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses.AlarmReceiver;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses.GlobalData;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseActivity extends AppCompatActivity {

    GlobalData gd;

    Toolbar toolbar;

    static final String BASE_URL = "http://192.168.1.52:3000/";
    //static final String BASE_URL = "http://192.168.1.11:3000/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    static String SHARED_PREFERENCES_FILE = "smcprefs";
    static int CAMERA_PERMISSION_CODE=100;
    public static final String ALARM_ACTION_INTENT = "smc.alarms";
    static int ALARM_CODE=300;

    public int menu_layout = R.menu.mainmenu_patient;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(menu_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.menuAdvancedMode:
                intent = new Intent(this, AdvancedModeActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuManagePatients:
                intent = new Intent(this, ManagePatientsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menuSettings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void loadToolbar(){
        if (gd==null){
            gd = (GlobalData) getApplicationContext();
        }

        //TODO remove later (test data)
        if (gd.getCurrentUser().getPatients().size()==0){
            gd.getCurrentUser().addPatient(new User("José"));
            gd.setActivePatient(gd.getCurrentUser().getPatients().get(0));
            gd.getCurrentUser().addPatient(new User("Ricardo"));
        }
        if (gd.userHasPatients()){
            if (gd.getActivePatient().getMedicines().size()==0){
                Medicine med = new Medicine();
                med.setName("med1");
                med.setQuantity(1);
                med.setExpirationDate(new Date());
                med.setNotes("notes");
                gd.getActivePatient().addMedicine(med);
            }
        }

        if (!gd.userHasCaretaker()){
            gd.getCurrentUser().setCaretaker(new User("Pedro"));
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        if (getSharedPreference("MODE")){
            menu_layout = R.menu.mainmenu_caretaker;
            invalidateOptionsMenu();
        }
    }
    public void makeToast(Object s){
        Toast.makeText(this, String.valueOf(s), Toast.LENGTH_SHORT).show();
    }

    public void logThis(Object s){
        Log.d("fds", String.valueOf(s));
    }

    public void writeSharedPreferences(String key, Boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences(SHARED_PREFERENCES_FILE,Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public Boolean getSharedPreference(String key){
        SharedPreferences sharedPref = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        Boolean b = sharedPref.getBoolean(key, false);
        return b;
    }

    public String friendlyDateTimeFormat(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return dateTime.format(formatter);
    }

    public void createAlarm(Prescription p) {

        AlarmManager am = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        long firstTime = System.currentTimeMillis()+1000*5;

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, firstTime, sender);
        AlarmReceiver alarmReceiver = new AlarmReceiver();
        alarmReceiver.setActivity(BaseActivity.this);
        alarmReceiver.setUpNextAlarm(BaseActivity.this);
    }
    public static void createNotification(Context x){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(x, "notify_001");
        Intent ii = new Intent(x, AlarmActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(x, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("xd");
        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setFullScreenIntent(pendingIntent, true);
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager = (NotificationManager) x.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("mychannelid", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            if(alarmSound != null){
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();
                notificationChannel.setSound(alarmSound,audioAttributes);
            }

            assert mNotificationManager != null;
            mBuilder.setChannelId("mychannelid");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }
}
