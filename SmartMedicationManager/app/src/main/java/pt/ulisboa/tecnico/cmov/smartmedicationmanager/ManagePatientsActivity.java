package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.PatientListAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;

public class ManagePatientsActivity extends BaseActivity {

    ListView patientList;
    Button selfAssignBt;
    Button addNewPatActivBt;
    Intent intent;
    PatientListAdapter adapter;
    List<User> patients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patients);
        loadToolbar();

        patientList = findViewById(R.id.patientList);
        selfAssignBt = findViewById(R.id.btAssignSelf);
        addNewPatActivBt = findViewById(R.id.btAddPatActiv);

        patients.addAll(gd.getCurrentUser().getPatients());
        adapter = new PatientListAdapter(this, R.layout.patient_list_item, patients, gd);

        patientList.setAdapter(adapter);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        selfAssignBt.setOnClickListener(v -> {
            alertDialog.setMessage("Are you sure?");
            alertDialog.setTitle("Self-assign");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (dialog, which) -> {
                        gd.getCurrentUser().addPatient(gd.getCurrentUser());
                        gd.setActivePatient(gd.getCurrentUser());
                        refreshList();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                    (dialog, which) -> {
                        dialog.cancel();
                    });

            alertDialog.show();

        });

        addNewPatActivBt.setOnClickListener(v -> {
            intent = new Intent(this, AddPatientActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();


    }

    public void refreshList() {
        executor.execute(() -> {
            patients.clear();
            patients.addAll(gd.getCurrentUser().getPatients());
            handler.post(() -> {
                adapter.notifyDataSetChanged();

                if (gd.userHasPatients()){
                    selfAssignBt.setEnabled(true);
                    if (gd.getCurrentUser().getPatients().contains(gd.getCurrentUser())){
                        selfAssignBt.setEnabled(false);
                    }
                }
                else{
                    selfAssignBt.setEnabled(true);
                }
            });
        });
    }

    public void delete(User u) {
        gd.getCurrentUser().getPatients().remove(u);
        if (gd.getActivePatient()!=null){
            if (gd.getActivePatient().getUsername().equals(u.getUsername())){
                gd.setActivePatient(null);
            }
        }
        refreshList();
    }

    public void setActive(User u) {
        gd.setActivePatient(u);
        refreshList();
    }
}