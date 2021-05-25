package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.services.AlarmService;

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

        //TODO remove later, call server
        if (gd.getCurrentUser().getSchedule().size() == 0) {
            logThis("created fake past prescription");
            Prescription p = new Prescription();
            p.setId(id);
            p.setMedicine(new Medicine("Good med", 20));
            p.setQuantity(1);
            p.setStartDate(LocalDateTime.now());
            p.setEndDate(LocalDateTime.now().plusSeconds(30));
            p.setPeriodicity("test");
            p.setNotes("");
            gd.getCurrentUser().getSchedule().add(p);

        }
        Prescription p = gd.getCurrentUser().getSchedule().stream().
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
        });

        btDismiss.setOnClickListener(v -> {
            p.setAlarm(getApplicationContext());
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
}