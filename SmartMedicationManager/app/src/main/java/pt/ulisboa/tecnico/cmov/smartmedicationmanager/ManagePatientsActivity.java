package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.PatientListAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Call<ArrayList<String>> call = userApi.getAllPatient(gd.getCurrentUser().getUsername());

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<String>> call, @NonNull Response<ArrayList<String>> response) {
                if(response.code() == 200) {
                    gd.getCurrentUser().getPatients().clear();
                    for (String s : response.body()){
                        logThis("patient->>>"+s);
                        gd.getCurrentUser().addPatient(new User(s));
                    }
                    if (gd.getCurrentUser().getPatients().size()>0){
                        gd.setActivePatient(gd.getCurrentUser().getPatients().get(0));
                    }
                    refreshList();
                } else if (response.code() == 400) {
                    makeToast("Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<String>> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });

        patients.addAll(gd.getCurrentUser().getPatients());
        adapter = new PatientListAdapter(this, R.layout.patient_list_item, patients, gd);

        patientList.setAdapter(adapter);

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        selfAssignBt.setOnClickListener(v -> {
            alertDialog.setMessage("Are you sure?");
            alertDialog.setTitle("Self-assign");

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    (dialog, which) -> {

                        Call<Void> call32 = userApi.addPatient(gd.getCurrentUser().getUsername(), gd.getCurrentUser().getUsername());

                        call32.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                if(response.code() == 200) {
                                    makeToast("Success");
                                    gd.getCurrentUser().addPatient(gd.getCurrentUser());
                                    gd.setActivePatient(gd.getCurrentUser());
                                    refreshList();
                                } else if (response.code() == 400) {
                                    makeToast("Error");
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
                            }
                        });
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

        Call<Void> call = userApi.deletePatient(gd.getCurrentUser().getUsername(), u.getUsername());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    makeToast("Success");
                    gd.getCurrentUser().getPatients().remove(u);
                    if (gd.getActivePatient()!=null){
                        if (gd.getActivePatient().getUsername().equals(u.getUsername())){
                            gd.setActivePatient(null);
                        }
                    }
                    refreshList();
                } else if (response.code() == 400) {
                    makeToast("Error");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });

    }

    public void setActive(User u) {
        gd.setActivePatient(u);
        refreshList();
    }
}