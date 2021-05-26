package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.PrescriptionAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

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

        //remove these two, setPrescriptions(response), refreshlist
        schedule.addAll(gd.getActivePatient().getPrescriptions());
        schedule.sort(Comparator.comparing(Prescription::getEndDate));

        adapter=new PrescriptionAdapter(this, R.layout.prescription_list_item, schedule);
        listView.setAdapter(adapter);


        fab.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddPrescriptionActivity.class);
            intent.putExtra("mode", -1);
            startActivity(intent);
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
        gd.getActivePatient().deletePrescription(p, getApplicationContext());
        refreshList();
    }
}