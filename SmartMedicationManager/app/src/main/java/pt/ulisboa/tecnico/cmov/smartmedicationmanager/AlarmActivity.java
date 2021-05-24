package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.services.AlarmService;

public class AlarmActivity extends BaseActivity {
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        loadToolbar();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        String times = intent.getStringExtra("time");

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

        TextView alarm = findViewById(R.id.alarmID);
        TextView time = findViewById(R.id.time);
        ImageView image = findViewById(R.id.imageView);
        TextView name = findViewById(R.id.name);
        TextView quantity = findViewById(R.id.quantity);
        TextView notes = findViewById(R.id.notes);

        TextView tvDoor = findViewById(R.id.textViewDoor);
        Button btDismiss = findViewById(R.id.btDismiss);

        alarm.setText(id);
        time.setText(times);
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
        });

        btDismiss.setOnClickListener(v -> {

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