package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDateTime;

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

            if (gd.userHasCaretaker()){
                welcome.setText("Assigned caretaker: "+gd.getCurrentUser().getCaretaker().getUsername()+"\n\nWelcome "+gd.getCurrentUser().getUsername());
                //todo ask data from server and create alarms
                for (Prescription p : gd.getCurrentUser().getSchedule()){
                    if (!p.getPeriodicity().equals("test")){
                        //makeToast("updating alarm");
                        //p.setAlarm(getApplicationContext(), gd.getCurrentUser().getSchedule().indexOf(p));
                    }
                }
            }
            else{
                welcome.setText("No assigned caretaker. Ask someone to assign you or switch to advanced mode.\n\nWelcome "+gd.getCurrentUser().getUsername());
                scheduleBt.setEnabled(false);
                helpBt.setEnabled(false);

            }


            scheduleBt.setOnClickListener(v -> {
                makeToast("Schedule");
                //TODO remove later (test data)
                if (gd.getCurrentUser().getSchedule().size()==0){
                    Prescription p = new Prescription();
                    p.generateId();
                    p.setMedicine(new Medicine("Good med", 20));
                    p.setQuantity(1);
                    p.setStartDate(LocalDateTime.now());
                    p.setEndDate(LocalDateTime.now().plusSeconds(30));
                    p.setPeriodicity("test");
                    p.setNotes("");
                    gd.getCurrentUser().addPrescription(p, getApplicationContext());
                    makeToast("created test alarm");
                }

            });

            helpBt.setOnClickListener(v -> {
                makeToast("Help");

            });

        }

    }
}