package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDateTime;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

public class MainActivity extends BaseActivity {
    int activityLayout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActivity();
    }

    public void loadActivity(){

        activityLayout = R.layout.activity_main_patient;
        if (getSharedPreference("MODE")){
            activityLayout = R.layout.activity_main_caretaker;
        }

        setContentView(activityLayout);
        loadToolbar();

        TextView welcome = findViewById(R.id.welcome);

        if (getSharedPreference("MODE")){
            ImageButton medicineBt = findViewById(R.id.btMedicine);
            ImageButton scheduleBt = findViewById(R.id.btSchedule);
            ImageButton alarmsBt = findViewById(R.id.btAlarms);
            ImageButton historyBt = findViewById(R.id.btHistory);
            if (gd.userHasPatients()){
                welcome.setText("Managing Patient "+gd.getActivePatient().getUsername());

            }
            else{
                welcome.setText("No patients assigned. Please access the Manage Patients setting to set yourself or someone else as patient.\n\nWelcome "+gd.getCurrentUser().getUsername());
                medicineBt.setEnabled(false);
                scheduleBt.setEnabled(false);
                alarmsBt.setEnabled(false);
                historyBt.setEnabled(false);
            }
            medicineBt.setOnClickListener(v -> {
                intent = new Intent(this, MedicineListActivity.class);
                startActivity(intent);

            });
            scheduleBt.setOnClickListener(v -> {
                intent = new Intent(this, ScheduleListActivity.class);
                startActivity(intent);

            });
            alarmsBt.setOnClickListener(v -> {
                makeToast("Alarms");

            });
            historyBt.setOnClickListener(v -> {
                makeToast("History");

            });

        }
        else{
            ImageButton scheduleBt = findViewById(R.id.buttonSchedule);
            ImageButton helpBt = findViewById(R.id.buttonHelp);
            ImageButton medicationBt = findViewById(R.id.btnMedication);
            ImageButton requestsBt = findViewById(R.id.btnRequests);

            if (gd.getCurrentUser().getSchedule().size()>1){
                for (Alarm alarm: gd.getCurrentUser().getSchedule().get(1).getAlarms()){
                    logThis(alarm.getDateTime().toString() + alarm.isTaken());
                }
            }


            if (gd.userHasCaretaker()){
                welcome.setText("Assigned caretaker: "+gd.getCurrentUser().getCaretaker().getUsername());
                //todo ask data from server and create alarms
                for (Prescription p : gd.getCurrentUser().getSchedule()){
                    if (!p.getPeriodicity().equals("test")){
                        //makeToast("updating alarm");
                        //p.setAlarm(getApplicationContext(), gd.getCurrentUser().getSchedule().indexOf(p));
                    }
                }
            }
            else{
                welcome.setText("No assigned caretaker. Ask someone to assign you or switch to advanced mode.");
                scheduleBt.setEnabled(false);
                helpBt.setEnabled(false);

            }


            scheduleBt.setOnClickListener(v -> {
                makeToast("Schedule");
                //TODO remove later (test data)
                if (true){
                    Prescription p = new Prescription();
                    p.generateId();
                    p.setMedicine(new Medicine("Good med", 20));
                    p.setQuantity(1);
                    p.setStartDate(LocalDateTime.now().plusSeconds(5));
                    p.setEndDate(LocalDateTime.now().plusSeconds(30));
                    p.setPeriodicity("test");
                    p.setNotes("");
                    gd.getCurrentUser().addPrescription(p, getApplicationContext());
                    makeToast("created test alarm");
//                    Prescription p2 = new Prescription();
//                    p2.generateId();
//                    p2.setMedicine(new Medicine("Great med", 20));
//                    p2.setQuantity(2);
//                    p2.setStartDate(LocalDateTime.now().plusSeconds(10));
//                    p2.setEndDate(LocalDateTime.now().plusSeconds(30));
//                    p2.setPeriodicity("test");
//                    p2.setNotes("");
//                    gd.getCurrentUser().addPrescription(p2, getApplicationContext());
                    for (Alarm alarm: p.getAlarms()){
                        logThis(alarm.getDateTime().toString() + alarm.isTaken());
                    }
                }

            });

            helpBt.setOnClickListener(v -> {
                makeToast("Help");

            });

            medicationBt.setOnClickListener(v -> {
                makeToast("Medication");

            });

            requestsBt.setOnClickListener(v -> {
                makeToast("Requests");

            });

        }

    }
}