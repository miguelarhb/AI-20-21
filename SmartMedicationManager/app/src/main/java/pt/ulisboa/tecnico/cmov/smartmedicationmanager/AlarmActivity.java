package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDateTime;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses.AlarmService;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

public class AlarmActivity extends BaseActivity {
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        loadToolbar();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        TextView alarm = findViewById(R.id.alarm);
        Button btStop = findViewById(R.id.btStop);
        TextView tvDoor = findViewById(R.id.textViewDoor);
        Button btDismiss = findViewById(R.id.btDismiss);

        alarm.setText(id);
        tvDoor.setText("Door: closed");
        tvDoor.setTextColor(Color.RED);

        tvDoor.setEnabled(false);
        btDismiss.setEnabled(false);

        btStop.setOnClickListener(v -> {

            Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
            getApplicationContext().stopService(intentService);

            tvDoor.setEnabled(true);
            btStop.setEnabled(false);

        });

        tvDoor.setOnClickListener(v -> {
            tvDoor.setText("Door: open");
            tvDoor.setTextColor(Color.GREEN);
            btDismiss.setEnabled(true);
        });

        btDismiss.setOnClickListener(v -> {
            //TODO remove later, call server
            if (gd.getCurrentUser().getSchedule().size()==0){
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

            //todo if end date not yet
            if (true){
                p.setAlarm(getApplicationContext(), gd.getCurrentUser().getSchedule().indexOf(p));
                makeToast("repeat alarm");
            }

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