package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.helperClasses.MedicineAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Medicine;

public class MedicineListActivity extends BaseActivity {

    List<Medicine> items= new ArrayList<>();
    MedicineAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);
        loadToolbar();

        listView = findViewById(R.id.medicineList);

        FloatingActionButton fab = findViewById(R.id.fabAddItem);

        items.addAll(gd.getCurrentUser().getMedicines());

        adapter=new MedicineAdapter(this, R.layout.medicine_list_item, items);
        listView.setAdapter(adapter);


        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddMedicineActivity.class);
            startActivity(intent);
        });
    }

    public void refreshList() {
        items.clear();
        items.addAll(gd.getCurrentUser().getMedicines());
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();

    }

    public void editMedicine(Medicine m) {
        //TODO
        Intent intent = new Intent(this, AddMedicineActivity.class);
        startActivity(intent);
    }

    public void deleteMedicine(Medicine m) {
        gd.getCurrentUser().getMedicines().remove(m);
        refreshList();
    }
}