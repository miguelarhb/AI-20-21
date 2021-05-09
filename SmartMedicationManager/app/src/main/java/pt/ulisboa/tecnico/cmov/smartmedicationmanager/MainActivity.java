package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses.GlobalData;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class MainActivity extends BaseActivity {
    int activityLayout;

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
        if (gd==null){
            gd = (GlobalData) getApplicationContext();
        }
        if (gd.getCurrentUser()==null){
            gd.setCurrentUser(new User("Paulo"));
        }

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
                logThis(gd.getCurrentUser().getUsername());
                logThis(gd.getCurrentUser().getPatients().size());
                logThis(gd.getCurrentUser().getPatients().get(0).getUsername());
                logThis(gd.getActivePatient());
                welcome.setText("Active Patient: "+gd.getActivePatient().getUsername() +"\n\nWelcome "+gd.getCurrentUser().getUsername());

            }
            else{
                welcome.setText("No patients assigned. Please access the Manage Patients setting to set yourself or someone else as patient.\n\nWelcome "+gd.getCurrentUser().getUsername());
                medicineBt.setEnabled(false);
                scheduleBt.setEnabled(false);
                alarmsBt.setEnabled(false);
                historyBt.setEnabled(false);
            }
            medicineBt.setOnClickListener(v -> {
                makeToast("Medicine");

            });
            scheduleBt.setOnClickListener(v -> {
                makeToast("Schedule");

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
            }
            else{
                welcome.setText("No assigned caretaker. Ask someone to assign you or switch to advanced mode.\n\nWelcome "+gd.getCurrentUser().getUsername());
                scheduleBt.setEnabled(false);
                helpBt.setEnabled(false);

            }


            scheduleBt.setOnClickListener(v -> {
                makeToast("Schedule");

            });

            helpBt.setOnClickListener(v -> {
                makeToast("Help");

            });

        }

    }
}