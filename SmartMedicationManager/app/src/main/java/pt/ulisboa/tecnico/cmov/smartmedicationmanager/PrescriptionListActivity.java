package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.PrescriptionAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionListActivity extends BaseActivity {

    List<Prescription> schedule= new ArrayList<>();
    PrescriptionAdapter adapter;
    ListView listView;

    boolean patient;
    int prescription_list_item_layout;

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        loadToolbar();

        patient = getIntent().getBooleanExtra("patient", false);

        listView = findViewById(R.id.scheduleList);

        FloatingActionButton fab = findViewById(R.id.fabAddPrescription);

        if (patient){
            username = gd.getCurrentUser().getUsername();
            fab.setVisibility(View.GONE);
            prescription_list_item_layout=R.layout.prescription_list_item_patient;
        }
        else{
            username = gd.getActivePatient().getUsername();
            prescription_list_item_layout=R.layout.prescription_list_item;
        }

        updateMedicinesFromServer();



        adapter=new PrescriptionAdapter(this, prescription_list_item_layout, schedule);
        listView.setAdapter(adapter);

        Call<ArrayList<Prescription>> call = prescriptionApi.getAllPrescription(username);

        call.enqueue(new Callback<ArrayList<Prescription>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Prescription>> call, @NonNull Response<ArrayList<Prescription>> response) {
                if(response.code() == 200) {
                    if (patient){
                        gd.getCurrentUser().setPrescriptions(response.body());
                    }
                    else{
                        gd.getActivePatient().setPrescriptions(response.body());
                    }
                    refreshList();
                } else if (response.code() == 400) {
                    makeToast("Fail Getting Prescription");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Prescription>> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddPrescriptionActivity.class);
            intent.putExtra("mode", -1);
            startActivity(intent);
        });
    }

    private void updateMedicinesFromServer() {

        Call<ArrayList<Medicine>> call = medicineApi.getAllMedicine(username);

        call.enqueue(new Callback<ArrayList<Medicine>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Medicine>> call, @NonNull Response<ArrayList<Medicine>> response) {
                if(response.code() == 200) {
                    if (patient){
                        gd.getCurrentUser().setMedicines(response.body());
                    }
                    else{
                        gd.getActivePatient().setMedicines(response.body());
                    }

                } else if (response.code() == 400) {
                    makeToast("Fail Getting Medicine");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Medicine>> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });
    }

    public void refreshList() {
        schedule.clear();
        if (patient){
            schedule.addAll(gd.getCurrentUser().getPrescriptions());
        }
        else{
            schedule.addAll(gd.getActivePatient().getPrescriptions());
        }
        schedule.sort(Comparator.comparing(Prescription::getEndDate));
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();

    }

    public void editPrescription(Prescription p) {
        int index = gd.getActivePatient().getPrescriptions().indexOf(p);
        Intent intent = new Intent(this, AddPrescriptionActivity.class);
        intent.putExtra("mode", index);
        startActivity(intent);
    }

    public void deletePrescription(Prescription p) {

        Call<Void> call = prescriptionApi.deletePrescription(gd.getActivePatient().getUsername(), p.getId());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    makeToast("Deleted Prescription");
                    gd.getActivePatient().getPrescriptions().remove(p);
                    p.cancelAlarm(getApplicationContext());
                    refreshList();
                } else if (response.code() == 400) {
                    makeToast("Fail Delete Prescription");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                if (!t.getMessage().equals("timeout")) { makeToast(t.getMessage()); }
            }
        });
    }
}