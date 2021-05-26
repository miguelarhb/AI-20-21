package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.MedicineAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineListActivity extends BaseActivity {

    List<Medicine> items= new ArrayList<>();
    MedicineAdapter adapter;
    ListView listView;

    boolean patient;
    int medicine_list_item_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        loadToolbar();

        patient = getIntent().getBooleanExtra("patient", false);

        listView = findViewById(R.id.medicineList);

        FloatingActionButton fab = findViewById(R.id.fabAddItem);

        if (patient){
            Call<ArrayList<Medicine>> call = medicineApi.getAllMedicine(gd.getCurrentUser().getUsername());

            call.enqueue(new Callback<ArrayList<Medicine>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Medicine>> call, @NonNull Response<ArrayList<Medicine>> response) {
                    if(response.code() == 200) {
                        items.clear();
                        items.addAll(response.body());
                        adapter.notifyDataSetChanged();
                        gd.getCurrentUser().setMedicines(response.body());
                    } else if (response.code() == 400) {
                        makeToast("Fail Getting Medicine");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Medicine>> call, @NonNull Throwable t) {
                    makeToast(t.getMessage());
                }
            });

            fab.setVisibility(View.GONE);
            medicine_list_item_layout=R.layout.medicine_list_item_patient;
        }
        else{
            Call<ArrayList<Medicine>> call = medicineApi.getAllMedicine(gd.getActivePatient().getUsername());

            call.enqueue(new Callback<ArrayList<Medicine>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Medicine>> call, @NonNull Response<ArrayList<Medicine>> response) {
                    if(response.code() == 200) {
                        items.clear();
                        items.addAll(response.body());
                        adapter.notifyDataSetChanged();
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
            medicine_list_item_layout=R.layout.medicine_list_item;
        }

        adapter=new MedicineAdapter(this, medicine_list_item_layout, items);
        listView.setAdapter(adapter);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddMedicineActivity.class);
            intent.putExtra("mode", -1);
            startActivity(intent);
        });
    }

    public void refreshList() {
        items.clear();
        if (patient){
            items.addAll(gd.getCurrentUser().getMedicines());
        }
        else{
            items.addAll(gd.getActivePatient().getMedicines());
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();
    }

    public void editMedicine(Medicine m) {
        int index = gd.getActivePatient().getMedicines().indexOf(m);
        Intent intent = new Intent(this, AddMedicineActivity.class);
        intent.putExtra("mode", index);
        startActivity(intent);
    }

    public void deleteMedicine(Medicine m) {
        Call<Void> call = medicineApi.deleteMedicine(gd.getCurrentUser().getUsername(), m.getName());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.code() == 200) {
                    makeToast("Deleted Medicine");
                    gd.getActivePatient().getMedicines().remove(m);
                } else if (response.code() == 400) {
                    makeToast("Fail Delete Medicine");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                makeToast(t.getMessage());
            }
        });

        refreshList();
    }
}