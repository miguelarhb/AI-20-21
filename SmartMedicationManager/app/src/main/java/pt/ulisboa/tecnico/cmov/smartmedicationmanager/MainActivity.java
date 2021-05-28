package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (getSharedPreferenceBoolean("MODE")){
            activityLayout = R.layout.activity_main_caretaker;
        }

        setContentView(activityLayout);
        loadToolbar();

        TextView welcome = findViewById(R.id.welcome);

        if (getSharedPreferenceBoolean("MODE")){

            Call<ArrayList<String>> call = userApi.getAllPatient(gd.getCurrentUser().getUsername());

            call.enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                    if(response.code() == 200) {
                        gd.getCurrentUser().getPatients().clear();
                        for (String s : response.body()){
                            gd.getCurrentUser().addPatient(new User(s));
                        }
                        if (gd.getCurrentUser().getPatients().size()>0){
                            gd.setActivePatient(gd.getCurrentUser().getPatients().get(0));
                        }
                    } else if (response.code() == 400) {
                        makeToast("Error");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                    if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
                }
            });

            ImageButton medicineBt = findViewById(R.id.btMedicineC);
            ImageButton prescriptionsBt = findViewById(R.id.btPrescriptionsC);
            ImageButton alarmsBt = findViewById(R.id.btAlarmsC);
            ImageButton scheduleBt = findViewById(R.id.btnScheduleC);
            if (gd.userHasPatients()){
                welcome.setText("Managing Patient "+gd.getActivePatient().getUsername());

            }
            else{
                welcome.setText("No patients assigned. Please access the Manage Patients setting to set yourself or someone else as patient.");
                medicineBt.setEnabled(false);
                scheduleBt.setEnabled(false);
                alarmsBt.setEnabled(false);
                prescriptionsBt.setEnabled(false);
            }
            medicineBt.setOnClickListener(v -> {
                intent = new Intent(this, MedicineListActivity.class);
                startActivity(intent);

            });
            prescriptionsBt.setOnClickListener(v -> {
                intent = new Intent(this, PrescriptionListActivity.class);
                startActivity(intent);

            });
            alarmsBt.setOnClickListener(v -> {
                intent = new Intent(this, CaretakerAlarmsActivity.class);
                startActivity(intent);

            });
            scheduleBt.setOnClickListener(v -> {
                intent = new Intent(this, ScheduleActivity.class);
                startActivity(intent);

            });

        }
        else{

            Call<String> call = userApi.getCaretaker(gd.getCurrentUser().getUsername());

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if(response.code() == 200) {
                        gd.getCurrentUser().setCaretaker(new User(response.body()));
                    } else if (response.code() == 400) {
                        makeToast("Error");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
                }
            });

            ImageButton scheduleBt = findViewById(R.id.btScheduleP);
            ImageButton prescriptionsBt = findViewById(R.id.buttonPrescriptionsP);
            ImageButton medicationBt = findViewById(R.id.btnMedicationP);
            ImageButton requestsBt = findViewById(R.id.btnRequestsP);
            Button testAlarmBt = findViewById(R.id.btTestAlarm);


            if (gd.userHasCaretaker()){
                welcome.setText("Assigned caretaker: "+gd.getCurrentUser().getCaretaker().getUsername());
                //todo ask data from server and create alarms
                for (Prescription p : gd.getCurrentUser().getPrescriptions()){
                    if (!p.getPeriodicity().equals("test")){
                        //makeToast("updating alarm");
                        //p.setAlarm(getApplicationContext(), gd.getCurrentUser().getSchedule().indexOf(p));
                    }
                }
            }
            else{
                welcome.setText("No assigned caretaker. Ask someone to assign you or switch to advanced mode.");
                scheduleBt.setEnabled(false);
                prescriptionsBt.setEnabled(false);
                medicationBt.setEnabled(false);
                testAlarmBt.setEnabled(false);

            }

            scheduleBt.setOnClickListener(v -> {
                intent = new Intent(this, ScheduleActivity.class);
                intent.putExtra("patient",true);
                startActivity(intent);

            });

            prescriptionsBt.setOnClickListener(v -> {
                intent = new Intent(this, PrescriptionListActivity.class);
                intent.putExtra("patient",true);
                startActivity(intent);

            });

            medicationBt.setOnClickListener(v -> {
                intent = new Intent(this, MedicineListActivity.class);
                intent.putExtra("patient",true);
                startActivity(intent);

            });

            requestsBt.setOnClickListener(v -> {
                intent = new Intent(this, RequestsActivity.class);
                startActivity(intent);

            });

            testAlarmBt.setOnClickListener(v -> {
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
                }

            });

        }

    }
}