package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.MedicineAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;

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
            items.addAll(gd.getCurrentUser().getMedicines());
            fab.setVisibility(View.GONE);
            medicine_list_item_layout=R.layout.medicine_list_item_patient;
        }
        else{
            items.addAll(gd.getActivePatient().getMedicines());
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
        items.addAll(gd.getActivePatient().getMedicines());
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
        gd.getActivePatient().getMedicines().remove(m);
        refreshList();
    }
}