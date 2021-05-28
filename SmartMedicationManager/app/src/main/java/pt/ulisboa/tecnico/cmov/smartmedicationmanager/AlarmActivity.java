package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.services.AlarmService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmActivity extends BaseActivity {
    String id;
    Alarm a = null;
    String periodicity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        loadToolbar();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        long timeMs = intent.getLongExtra("time", 0);
        String hm = new SimpleDateFormat("HH:mm").format(new Date(timeMs));
        Instant instant = Instant.ofEpochMilli(timeMs);
        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (gd.getCurrentUser().getPrescriptions().size() == 0) {
            getMedicinesAndPrescriptions(gd.getCurrentUser().getUsername(), true);

        }
        Prescription p = gd.getCurrentUser().getPrescriptions().stream().
                filter(o -> o.getId().equals(id)).
                findAny().orElse(null);
        periodicity = p.getPeriodicity();

        a = p.getAlarms().stream().
                filter(o -> o.getDateTime().isEqual(date)).
                findAny().orElse(null);

        a.setTaken(false);

        TextView alarm = findViewById(R.id.alarmID);
        TextView time = findViewById(R.id.time);
        ImageView image = findViewById(R.id.imageView);
        TextView name = findViewById(R.id.name);
        TextView quantity = findViewById(R.id.quantity);
        TextView notes = findViewById(R.id.notes);

        TextView tvDoor = findViewById(R.id.textViewDoor);
        Button btDismiss = findViewById(R.id.btDismiss);

        alarm.setText(id);
        time.setText(hm);
        //image.setImageBitmap();
        name.setText(p.getMedicine().getName());
        quantity.setText(String.valueOf(p.getQuantity()));
        notes.setText(p.getNotes());

        tvDoor.setText("Door: closed");
        tvDoor.setTextColor(Color.RED);
        btDismiss.setEnabled(false);

        Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(intentService);

        tvDoor.setOnClickListener(v -> {
            tvDoor.setText("Door: open");
            tvDoor.setTextColor(Color.GREEN);
            btDismiss.setEnabled(true);
            a.setTaken(true);
            p.getMedicine().setQuantity(p.getMedicine().getQuantity()-1);
            updatePrescription(p);
            p.setAlarm(getApplicationContext());
        });

        btDismiss.setOnClickListener(v -> {
            finish();
        });

//        btSnooze.setOnClickListener(v -> {
//            //hmm
////            Calendar calendar = Calendar.getInstance();
////            calendar.setTimeInMillis(System.currentTimeMillis());
////            calendar.add(Calendar.MINUTE, 10);
////
////            Alarm alarm = new Alarm(
////                    new Random().nextInt(Integer.MAX_VALUE),
////                    calendar.get(Calendar.HOUR_OF_DAY),
////                    calendar.get(Calendar.MINUTE),
////                    "Snooze",
////                    true,
////                    false,
////                    false,
////                    false,
////                    false,
////                    false,
////                    false,
////                    false,
////                    false
////            );
////
////            alarm.schedule(getApplicationContext());
//
//            Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
//            getApplicationContext().stopService(intentService);
//            finish();
//
//        });
    }

    private void updatePrescription(Prescription p) {

        String username = gd.getCurrentUser().getUsername();

        Call<Void> call = prescriptionApi.editPrescription(username, p.getId() , p);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    makeToast("Sent to server");
                } else if (response.code() == 400) {
                    makeToast("Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { logThis(t.getMessage()); }
            }
        });
    }
}