package pt.ulisboa.tecnico.cmov.smartmedicationmanager;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pt.ulisboa.tecnico.cmov.smartmedicationmanager.adapters.ScheduleAdapter;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Alarm;
import pt.ulisboa.tecnico.cmov.smartmedicationmanager.models.Prescription;

public class ScheduleActivity extends BaseActivity {

    List<Alarm> shownAlarms = new ArrayList<>();
    Map<Date, List<Alarm>> map = new TreeMap<>();
    Map<Alarm,Prescription> prescriptionMap = new HashMap<>();
    ScheduleAdapter adapter;
    ListView listView;
    int counter = 0;
    Date now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        loadToolbar();

        now = new Date();

        listView = findViewById(R.id.scheduleList);
        ImageButton previous = findViewById(R.id.previous);
        ImageButton next = findViewById(R.id.next);

        boolean patient = getIntent().getBooleanExtra("patient", false);

        List<Prescription> source;

        if (patient){
            source = gd.getCurrentUser().getSchedule();
        }
        else{
            source = gd.getActivePatient().getSchedule();
        }

        Date date;
        for (Prescription p: source){
            for (Alarm a : p.getAlarms()){
                date = toDate(a.getDateTime());
                if (map.get(date)==null){
                    List<Alarm> alarms = new ArrayList<>();
                    alarms.add(a);
                    prescriptionMap.put(a,p);
                    map.put(date, alarms);
                }
                else{
                    map.get(date).add(a);
                    prescriptionMap.put(a,p);
                }

            }
        }
        for (List l : map.values()){
            l.sort(Comparator.comparing(Alarm::getDateTime));
        }

        iterateMap();


        adapter = new ScheduleAdapter(this, R.layout.caretaker_alarm_list_item, shownAlarms, prescriptionMap);
        listView.setAdapter(adapter);

        previous.setOnClickListener(v -> {
            counter--;
            if (counter<0){
                counter=map.size()-1;
            }
            iterateMap();
        });

        next.setOnClickListener(v -> {
            counter++;
            if (counter>=map.size()){
                counter=0;
            }
            iterateMap();
        });
    }

    private void iterateMap() {
        int i = 0;
        for (Date day : map.keySet()){
            if (counter==0){
                if (day.after(now)){
                    shownAlarms = map.get(day);
                    counter=i;
                    return;
                }
            }
            else{
                if (i == counter){
                    shownAlarms = map.get(day);
                    return;
                }
            }
            i++;
        }

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

    public Date toDate(LocalDateTime dateTime){
        return Date.from(dateTime.atZone(ZoneId.systemDefault())
                .toInstant());
    }
}