package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_list);
        loadToolbar();

        listView = findViewById(R.id.scheduleList);

        FloatingActionButton fab = findViewById(R.id.fabAddPrescription);

        updateMedicinesFromServer();

        Call<ArrayList<Prescription>> call = prescriptionApi.getAllPrescription(gd.getActivePatient().getUsername());

        call.enqueue(new Callback<ArrayList<Prescription>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Prescription>> call, @NonNull Response<ArrayList<Prescription>> response) {
                if(response.code() == 200) {
                    gd.getActivePatient().setPrescriptions(response.body());
                    refreshList();
                } else if (response.code() == 400) {
                    makeToast("Fail Getting Prescription");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Prescription>> call, @NonNull Throwable t) {
                makeToast(t.getMessage());
            }
        });

        adapter=new PrescriptionAdapter(this, R.layout.prescription_list_item, schedule);
        listView.setAdapter(adapter);


        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddPrescriptionActivity.class);
            intent.putExtra("mode", -1);
            startActivity(intent);
        });
    }

    private void updateMedicinesFromServer() {
        Call<ArrayList<Medicine>> call = medicineApi.getAllMedicine(gd.getActivePatient().getUsername());

        call.enqueue(new Callback<ArrayList<Medicine>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Medicine>> call, @NonNull Response<ArrayList<Medicine>> response) {
                if(response.code() == 200) {
                    gd.getActivePatient().setMedicines(response.body());
                } else if (response.code() == 400) {
                    makeToast("Fail Getting Medicine");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Medicine>> call, @NonNull Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }

    public void refreshList() {
        schedule.clear();
        schedule.addAll(gd.getActivePatient().getPrescriptions());
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
                    gd.getActivePatient().deletePrescription(p, getApplicationContext());
                    refreshList();
                } else if (response.code() == 400) {
                    makeToast("Fail Delete Prescription");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                makeToast(t.getMessage());
            }
        });
    }
}