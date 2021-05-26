package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.CaretakerAlarmsAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

public class CaretakerAlarmsActivity extends BaseActivity {

    List<Alarm> alarms = new ArrayList<>();
    Map<Alarm,Prescription> map = new HashMap<>();
    CaretakerAlarmsAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_alarms);
        loadToolbar();

        LocalDateTime now = LocalDateTime.now();

        listView = findViewById(R.id.caretakerAlarms);

        for (Prescription p: gd.getActivePatient().getPrescriptions()){
            for (Alarm a : p.getAlarms()){
                if (a.getDateTime().isBefore(now) && !a.isTaken()){
                    alarms.add(a);
                    map.put(a, p);
                }

            }
        }
        alarms.sort(Comparator.comparing(Alarm::getDateTime));

        adapter = new CaretakerAlarmsAdapter(this, R.layout.caretaker_alarm_list_item, alarms, map);
        listView.setAdapter(adapter);
    }

    public void refreshList() {
        //
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();

    }
}